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

package cn.enbug.shop.common;

import cn.enbug.shop.admin.AdminRoutes;
import cn.enbug.shop.common.directive.CompressDirective;
import cn.enbug.shop.common.handler.StaticHandler;
import cn.enbug.shop.common.interceptor.LogInterceptor;
import cn.enbug.shop.common.kit.DruidKit;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.ShortMessageKit;
import cn.enbug.shop.common.model.MappingKit;
import cn.enbug.shop.common.plugin.oss.OssPlugin;
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
import redis.clients.jedis.JedisPoolConfig;

/**
 * This is the config of JFinal.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.13
 * @see com.jfinal.config.JFinalConfig
 * @since 1.0.0
 */
public class Config extends JFinalConfig {

    /**
     * Global config.
     */
    private static final Prop P = loadConfig();

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
        String url = P.get("postgres.url");
        String username = P.get("postgres.username");
        String password = P.get("postgres.password");
        String driverClass = P.get("postgres.driverClass");
        DruidPlugin dp = new DruidPlugin(url, username, password, driverClass);
        if (null == wallFilter) {
            wallFilter = new WallFilter();
        }
        wallFilter.setDbType(JdbcConstants.POSTGRESQL);
        StatFilter statFilter = new StatFilter();
        dp.addFilter(wallFilter);
        dp.addFilter(statFilter);
        return dp;
    }

    /**
     * config the redis pool.
     *
     * @param rp RedisPlugin
     * @return rp self
     */
    private RedisPlugin configRedisPlugin(RedisPlugin rp) {
        JedisPoolConfig config = rp.getJedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        // 设置最小空闲数
        config.setMinIdle(8);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // Idle时进行连接扫描
        config.setTestWhileIdle(true);
        // 表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        // 表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
        return rp;
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
        MappingKit.mapping(arp);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
        arp.addSqlTemplate("all.sql");
        return arp;
    }

    private OssPlugin getOssPlugin() {
        String bucketName = "shopmanagement";
        String endpoint = PropKit.get("ossWriter.endpoint");
        String key = PropKit.get("ossWriter.key");
        String secret = PropKit.get("ossWriter.secret");
        return new OssPlugin(bucketName, endpoint, key, secret);
    }

    /**
     * Get RedisPlugin.
     *
     * @param cacheName cache name
     * @param database  database
     * @return RedisPlugin Object
     */
    private RedisPlugin getRedisPlugin(String cacheName, int database) {
        String host = P.get("redis.host");
        int port = P.getInt("redis.port");
        int timeout = P.getInt("redis.timeout");
        String password = P.get("redis.password");
        RedisPlugin rp = new RedisPlugin(cacheName, host, port, timeout, password, database);
        return configRedisPlugin(rp);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configConstant(Constants)
     */
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(P.getBoolean("devMode", false));
        me.setJsonFactory(new MixedJsonFactory());
        me.setViewType(ViewType.JFINAL_TEMPLATE);
        me.setBaseUploadPath(P.get("baseUploadFile"));
        me.setBaseDownloadPath(P.get("baseDownloadFile"));
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configRoute(Routes)
     */
    @Override
    public void configRoute(Routes me) {
        me.add(new FrontRoutes());
        me.add(new AdminRoutes());
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
        me.add(getRedisPlugin(RedisKit.TOKEN, P.getInt("redis.database.token")));
        me.add(getRedisPlugin(RedisKit.SHORT_MESSAGE_CAPTCHA, P.getInt("redis.database.captcha")));
        me.add(getRedisPlugin(RedisKit.ACTIVE_CODE_FOR_PHONE_NUMER, P.getInt("redis.database.activePhone")));
        me.add(getRedisPlugin(RedisKit.ACTIVE_CODE_FOR_EMAIL, P.getInt("redis.database.activeEmail")));
        me.add(getOssPlugin());
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configInterceptor(Interceptors)
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new LogInterceptor());
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
        String url = P.get("aldy.url");
        String appkey = P.get("aldy.appkey");
        String secret = P.get("aldy.secret");
        ShortMessageKit.init(url, appkey, secret);
    }

}
