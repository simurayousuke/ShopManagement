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

package cn.enbug.shop.common.kit;

import cn.enbug.shop.common.model.MappingKit;
import cn.enbug.shop.common.plugin.opensearch.OpenSearchPlugin;
import cn.enbug.shop.common.plugin.oss.OssPlugin;
import cn.enbug.shop.common.service.RsaService;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.aliyun.opensearch.object.KeyTypeEnum;
import com.jfinal.config.Plugins;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JFinal插件
 *
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class PluginKit {

    private static final RsaService RSA_SERVICE = RsaService.ME;
    /**
     * Firewall for the database in case to avoid from sql injections.
     */
    private static WallFilter wallFilter;

    private PluginKit() {

    }

    /**
     * Get the Druid Plugin added with WallFilter and StatFilter.
     *
     * @return DruidPlugin for Postgres
     */
    public static DruidPlugin getDruidPlugin() {
        String url = RSA_SERVICE.decrypt(PropKit.get("postgres.url"));
        String username = RSA_SERVICE.decrypt(PropKit.get("postgres.username"));
        String password = RSA_SERVICE.decrypt(PropKit.get("postgres.password"));
        String driverClass = PropKit.get("postgres.driverClass");
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

    public static void addPlugin(Plugins me) {
        DruidPlugin dp = getDruidPlugin();
        me.add(dp);
        me.add(getActiveRecordPlugin(dp));
        me.add(getRedisPlugin(RedisKit.TOKEN, PropKit.getInt("redis.database.token")));
        me.add(getRedisPlugin(RedisKit.SHORT_MESSAGE_CAPTCHA, PropKit.getInt("redis.database.messageCaptcha")));
        me.add(getRedisPlugin(RedisKit.ACTIVE_CODE_FOR_PHONE_NUMBER, PropKit.getInt("redis.database.activePhone")));
        me.add(getRedisPlugin(RedisKit.ACTIVE_CODE_FOR_EMAIL, PropKit.getInt("redis.database.activeEmail")));
        me.add(getRedisPlugin(RedisKit.IMAGE_CAPTCHA, PropKit.getInt("redis.database.imageCaptcha")));
        me.add(getRedisPlugin(RedisKit.ORDER_ID, PropKit.getInt("redis.database.orderId")));
        me.add(getOssPlugin());
        me.add(getCron4jPlugin());
        me.add(getOpenSearchPlugin());
    }

    /**
     * config the redis pool.
     *
     * @param rp RedisPlugin
     * @return rp self
     */
    private static RedisPlugin configRedisPlugin(RedisPlugin rp) {
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
    private static ActiveRecordPlugin getActiveRecordPlugin(DruidPlugin dp) {
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setDialect(new PostgreSqlDialect());
        MappingKit.mapping(arp);
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/sql");
        arp.addSqlTemplate("all.sql");
        return arp;
    }

    private static OssPlugin getOssPlugin() {
        String bucketName = "shopmanagement";
        String endpoint = PropKit.get("ossWriter.endpoint");
        String key = RSA_SERVICE.decrypt(PropKit.get("ossWriter.key"));
        String secret = RSA_SERVICE.decrypt(PropKit.get("ossWriter.secret"));
        return new OssPlugin(bucketName, endpoint, key, secret);
    }

    /**
     * Get RedisPlugin.
     *
     * @param cacheName cache name
     * @param database  database
     * @return RedisPlugin Object
     */
    private static RedisPlugin getRedisPlugin(String cacheName, int database) {
        String host = RSA_SERVICE.decrypt(PropKit.get("redis.host"));
        int port = Integer.parseInt(RSA_SERVICE.decrypt(PropKit.get("redis.port")));
        int timeout = PropKit.getInt("redis.timeout");
        String password = RSA_SERVICE.decrypt(PropKit.get("redis.password"));
        RedisPlugin rp = new RedisPlugin(cacheName, host, port, timeout, password, database);
        return configRedisPlugin(rp);
    }

    private static Cron4jPlugin getCron4jPlugin() {
        return new Cron4jPlugin(PropKit.getProp());
    }

    public static void afterPluginStart() {
        setWallFilter();
    }

    private static void setWallFilter() {
        if (null != wallFilter) {
            WallConfig wallConfig = wallFilter.getConfig();
            wallConfig.setSelectUnionCheck(false);
            wallConfig.setMultiStatementAllow(true);
            wallConfig.setNoneBaseStatementAllow(true);
            wallConfig.setMergeAllow(true);
        }
    }

    private static OpenSearchPlugin getOpenSearchPlugin() {
        String accessKey = RsaService.ME.decrypt(PropKit.get("openSearcher.key"));
        String secret = RsaService.ME.decrypt(PropKit.get("openSearcher.secret"));
        String host = PropKit.get("openSearcher.host");
        return new OpenSearchPlugin(accessKey, secret, host, KeyTypeEnum.ALIYUN);
    }

}
