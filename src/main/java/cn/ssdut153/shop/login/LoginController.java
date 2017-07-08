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

package cn.ssdut153.shop.login;

import cn.ssdut153.shop.captcha.ImageCaptchaValidator;
import cn.ssdut153.shop.common.controller.BaseController;
import cn.ssdut153.shop.common.kit.IpKit;
import cn.ssdut153.shop.common.kit.RedisKit;
import cn.ssdut153.shop.common.kit.Ret;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * 登录
 *
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({NoUrlPara.class})
public class LoginController extends BaseController {

    private static final LoginService srv = LoginService.me;

    /**
     * 登录页面
     */
    @Before({GET.class})
    public void index() {
        render("index.html");
    }

    /**
     * 用户名登录
     */
    @Before({POST.class, ImageCaptchaValidator.class, UsernameLoginValidator.class})
    public void username() {
        String username = getPara("username");
        String password = getPara("password");
        String ip = IpKit.getRealIp(getRequest());
        Ret ret = srv.loginByUsername(username, password, ip);
        if (ret.isSucceed()) {
            String token = ret.getAs("token");
            setCookie(RedisKit.COOKIE_ID, token, 60 * 60);
        }
        renderJson(ret);
    }

    /**
     * 手机号登录
     */
    @Before({POST.class, PhoneLoginValidator.class})
    public void phone() {
        String phone = getPara("phone");
        String captcha = getPara("phone_captcha");
        String ip = IpKit.getRealIp(getRequest());
        Ret ret = srv.loginByPhone(phone, captcha, ip);
        if (ret.isSucceed()) {
            String token = ret.getAs("token");
            setCookie(RedisKit.COOKIE_ID, token, 60 * 60);
        }
        renderJson(ret);
    }

    /**
     * 邮箱登录
     */
    @Before({POST.class, ImageCaptchaValidator.class, EmailLoginValidator.class})
    public void email() {
        String email = getPara("email");
        String password = getPara("password");
        String ip = IpKit.getRealIp(getRequest());
        Ret ret = srv.loginByEmail(email, password, ip);
        if (ret.isSucceed()) {
            String token = ret.getAs("token");
            setCookie(RedisKit.COOKIE_ID, token, 60 * 60);
        }
        renderJson(ret);
    }

}