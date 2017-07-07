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

/**
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginService {

    public static final LoginService me = new LoginService();

    /**
     * 用户名登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回信息
     */
    public Ret loginByUsername(String username, String password) {
        return null;
    }


    /**
     * 邮箱登录
     *
     * @param email   邮箱
     * @param captcha 验证码
     * @return 返回信息
     */
    public Ret loginByEmail(String email, String captcha) {
        return null;
    }

    /**
     * 手机号登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 返回信息
     */
    public Ret loginByPhone(String phone, String password) {
        return null;
    }

}
