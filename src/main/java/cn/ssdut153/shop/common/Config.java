/*
 * Copyright (c) 2017 EnBug Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ssdut153.shop.common;

import cn.ssdut153.shop.common.handler.StaticHandler;
import cn.ssdut153.shop.common.kit.DruidKit;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * This is the config of JFinal.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @see com.jfinal.config.JFinalConfig
 * @since 1.0.0
 */
public class Config extends JFinalConfig {

    /**
     * Global config.
     */
    private static final Prop p = loadConfig();

    /**
     * Firewall for the database in case to avoid from sql injections.
     */
    private static WallFilter wallFilter;

    /**
     * Get the global config from file.
     *
     * @return Global config
     */
    private static Prop loadConfig() {
        try {
            return PropKit.use("config-dev.properties");
        } catch (RuntimeException e) {
            LogKit.logNothing(e);
            return PropKit.use("config.properties");
        }
    }

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 8080, "/");
    }

    /**
     * Get the Druid Plugin added with WallFilter and StatFilter.
     *
     * @return DruidPlugin for Postgres
     */
    public static DruidPlugin getDruidPlugin() {
        DruidPlugin dp = new DruidPlugin(p.get("postgres.url"), p.get("postgres.username"), p.get("postgres.password"), p.get("postgres.driverClass"));
        if (null == wallFilter)
            wallFilter = new WallFilter();
        wallFilter.setDbType(JdbcConstants.POSTGRESQL);
        StatFilter statFilter = new StatFilter();
        dp.addFilter(wallFilter);
        dp.addFilter(statFilter);
        return dp;
    }

    /**
     * Get ActiveRecordPlugin.
     *
     * @param dp DruidPlugin returned by getDruidPlugin
     * @return ActiveRecordPlugin Object
     */
    private ActiveRecordPlugin getActiveRecordPlugin(DruidPlugin dp) {
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setDialect(new PostgreSqlDialect());
//        _MappingKit.mapping(arp);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
        arp.addSqlTemplate("all.sql");
        return arp;
    }

    /**
     * Get RedisPlugin.
     *
     * @return RedisPlugin Object
     */
    private RedisPlugin getRedisPlugin() {
        return new RedisPlugin("token", p.get("redis.host"), p.getInt("redis.port"), p.getInt("redis.timeOut"), p.get("redis.password"), p.getInt("redis.database.token"));
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configConstant(Constants)
     */
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setJsonFactory(new MixedJsonFactory());
        me.setViewType(ViewType.JFINAL_TEMPLATE);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configRoute(Routes)
     */
    public void configRoute(Routes me) {
        me.add(new FrontRoutes());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configEngine(Engine)
     */
    public void configEngine(Engine me) {
        me.addSharedFunction("_view/_common/_layout.html");
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configPlugin(Plugins)
     */
    public void configPlugin(Plugins me) {
        DruidPlugin dp = getDruidPlugin();
        me.add(dp);
        me.add(getActiveRecordPlugin(dp));
        me.add(getRedisPlugin());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configInterceptor(Interceptors)
     */
    public void configInterceptor(Interceptors me) {

    }

    /**
     * @see com.jfinal.config.JFinalConfig#configHandler(Handlers)
     */
    public void configHandler(Handlers me) {
        me.add(DruidKit.getDruidStatViewHandler());
        me.add(new StaticHandler());
    }

}
