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

import cn.ssdut153.shop.common.kit.Ret;
import cn.ssdut153.shop.common.service.UserService;

/**
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class LoginService {

    public static final LoginService me = new LoginService();
    private static final UserService srv = UserService.getInstance();

    /**
     * 用户名登录
     *
     * @param username 用户名
     * @param password 密码
     * @param ip       ip地址
     * @return 返回信息
     */
    public Ret loginByUsername(String username, String password, String ip) {
        String token = srv.login(username, password, ip);
        return Ret.succeed().set("token", token);
    }

    /**
     * 邮箱登录
     *
     * @param email    邮箱
     * @param password 密码
     * @param ip       ip地址
     * @return 返回信息
     */
    public Ret loginByEmail(String email, String password, String ip) {
        return Ret.fail();
    }

    /**
     * 手机号登录
     *
     * @param phone   手机号
     * @param captcha 验证码
     * @param ip      ip地址
     * @return 返回信息
     */
    public Ret loginByPhone(String phone, String captcha, String ip) {
        String token = srv.loginByPhone(phone, captcha, ip);
        return Ret.succeed().set("token", token);
    }

}
