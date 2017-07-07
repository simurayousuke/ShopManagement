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

import cn.ssdut153.shop.common.controller.BaseController;
import cn.ssdut153.shop.common.kit.Ret;

/**
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginController extends BaseController {

    private static final LoginService srv = LoginService.me;

    /**
     * 加载页面
     */
    public void index() {
        render("index.html");
    }

    /**
     * 用户名登录
     */
    public void username() {
        String username = getPara("username");
        String password = getPara("password");
        Ret ret = srv.loginByUsername(username, password);
        renderJson(ret);
    }

    /**
     * 手机号登录
     */
    public void phone() {
        String phone = getPara("phone");
        String captcha = getPara("captcha");
        Ret ret = srv.loginByPhone(phone, captcha);
        renderJson(ret);
    }

    /**
     * 邮箱登录
     */
    public void email() {
        String email = getPara("email");
        String password = getPara("password");
        Ret ret = srv.loginByEmail(email, password);
        renderJson(ret);
    }

}
