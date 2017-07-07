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

import cn.ssdut153.shop.common.directive.CompressDirective;
import cn.ssdut153.shop.common.handler.StaticHandler;
import cn.ssdut153.shop.common.kit.DruidKit;
import cn.ssdut153.shop.common.kit.ShortMessageKit;
import cn.ssdut153.shop.common.model._MappingKit;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
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
 * @author Hu Wenqiang
 * @version 1.0.6
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

    /**
     * Get the Druid Plugin added with WallFilter and StatFilter.
     *
     * @return DruidPlugin for Postgres
     */
    public static DruidPlugin getDruidPlugin() {
        String url = p.get("postgres.url");
        String username = p.get("postgres.username");
        String password = p.get("postgres.password");
        String driverClass = p.get("postgres.driverClass");
        DruidPlugin dp = new DruidPlugin(url, username, password, driverClass);
        if (null == wallFilter) {
            wallFilter = new WallFilter();
        }
        wallFilter.setDbType(JdbcConstants.POSTGRESQL);
        StatFilter statFilter = new StatFilter();
        dp.addFilter(wallFilter);
        dp.addFilter(statFilter);
        dp.setMinIdle(2);
        dp.setInitialSize(3);
        dp.setMaxActive(5);
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
        _MappingKit.mapping(arp);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
        arp.addSqlTemplate("all.sql");
        return arp;
    }

    /**
     * Get RedisPlugin.
     *
     * @return RedisPlugin Object for token
     */
    private RedisPlugin getTokenRedisPlugin() {
        String cacheName = "token";
        String host = p.get("redis.host");
        int port = p.getInt("redis.port");
        int timeout = p.getInt("redis.timeout");
        String password = p.get("redis.password");
        int database = p.getInt("redis.database.token");
        return new RedisPlugin(cacheName, host, port, timeout, password, database);
    }

    /**
     * Get RedisPlugin.
     *
     * @return RedisPlugin Object for captcha
     */
    private RedisPlugin getCaptchaedisPlugin() {
        String cacheName = "shortMessageCaptcha";
        String host = p.get("redis.host");
        int port = p.getInt("redis.port");
        int timeout = p.getInt("redis.timeout");
        String password = p.get("redis.password");
        int database = p.getInt("redis.database.captcha");
        return new RedisPlugin(cacheName, host, port, timeout, password, database);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configConstant(Constants)
     */
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(p.getBoolean("devMode", false));
        me.setJsonFactory(new MixedJsonFactory());
        me.setViewType(ViewType.JFINAL_TEMPLATE);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configRoute(Routes)
     */
    @Override
    public void configRoute(Routes me) {
        me.add(new FrontRoutes());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configEngine(Engine)
     */
    @Override
    public void configEngine(Engine me) {
        me.addSharedFunction("_view/_common/_layout.html");
        me.addDirective("compress", new CompressDirective());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configPlugin(Plugins)
     */
    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin dp = getDruidPlugin();
        me.add(dp);
        me.add(getActiveRecordPlugin(dp));
        me.add(getTokenRedisPlugin());
        me.add(getCaptchaedisPlugin());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configInterceptor(Interceptors)
     */
    @Override
    public void configInterceptor(Interceptors me) {

    }

    /**
     * @see com.jfinal.config.JFinalConfig#configHandler(Handlers)
     */
    @Override
    public void configHandler(Handlers me) {
        me.add(DruidKit.getDruidStatViewHandler());
        me.add(new StaticHandler());
    }

    @Override
    public void afterJFinalStart() {
        setWallFilter();
        initShortMessageKit();
    }

    private void setWallFilter() {
        if (null != wallFilter) {
            WallConfig wallConfig = wallFilter.getConfig();
            wallConfig.setSelectUnionCheck(false);
            wallConfig.setMultiStatementAllow(true);
            wallConfig.setNoneBaseStatementAllow(true);
            wallConfig.setMergeAllow(true);
        }
    }

    private void initShortMessageKit() {
        String url = p.get("aldy.url");
        String appkey = p.get("aldy.appkey");
        String secret = p.get("aldy.secret");
        ShortMessageKit.getMe().init(url, appkey, secret);
    }

}
