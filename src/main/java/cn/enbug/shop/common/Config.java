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
import cn.enbug.shop.captcha.RedisCaptchaCache;
import cn.enbug.shop.common.directive.CompressDirective;
import cn.enbug.shop.common.handler.StaticHandler;
import cn.enbug.shop.common.interceptor.ExceptionInterceptor;
import cn.enbug.shop.common.kit.DruidKit;
import cn.enbug.shop.common.kit.PluginKit;
import cn.enbug.shop.common.kit.ShortMessageKit;
import cn.enbug.shop.common.render.ShopRenderFactory;
import cn.enbug.shop.common.service.RsaService;
import com.jfinal.captcha.CaptchaManager;
import com.jfinal.config.*;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * This is the config of JFinal.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.1
 * @see com.jfinal.config.JFinalConfig
 * @since 1.0.0
 */
public class Config extends JFinalConfig {

    /**
     * Global config.
     */
    private static final Prop P = loadConfig();
    private static final RsaService RSA_SERVICE = RsaService.ME;

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
        return PluginKit.getDruidPlugin();
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
        me.setRenderFactory(new ShopRenderFactory());
        me.setCaptchaCache(new RedisCaptchaCache());
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
        String scheme = P.get("server.scheme");
        String host = P.get("server.host");
        String port = P.get("server.port");
        String path = P.get("server.path");
        String basePath = scheme + "://" + host + ("".equals(port) ? "" : ":" + port) + path;
        me.addSharedObject("basePath", basePath);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configPlugin(Plugins)
     */
    @Override
    public void configPlugin(Plugins me) {
        PluginKit.addPlugin(me);
    }

    /**
     * @see com.jfinal.config.JFinalConfig#configInterceptor(Interceptors)
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new ExceptionInterceptor());
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
        PluginKit.afterPluginStart();
        initShortMessageKit();
        CaptchaManager.me().setCaptchaCache(new RedisCaptchaCache());
    }

    private void initShortMessageKit() {
        String url = P.get("aldy.url");
        String appkey = RSA_SERVICE.decrypt(P.get("aldy.appkey"));
        String secret = RSA_SERVICE.decrypt(P.get("aldy.secret"));
        ShortMessageKit.init(url, appkey, secret);
    }

}
