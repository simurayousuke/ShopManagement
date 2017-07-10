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

package cn.enbug.shop.login;

import cn.enbug.shop.common.exception.LogException;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录服务
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.5
 * @since 1.0.0
 */
class LoginService {

    static final LoginService ME = new LoginService();
    private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);
    private static final UserService SRV = UserService.getInstance();
    private static final String INTERNAL_EXCEPTION = "internal exception";

    /**
     * 用户名登录
     *
     * @param username 用户名
     * @param password 密码
     * @param ip       ip地址
     * @return 返回信息
     */
    Ret loginByUsername(String username, String password, String ip) {
        try {
            String token = SRV.login(username, password, ip);
            if (null == token) {
                return Ret.fail("username or password wrong");
            }
            return Ret.succeed().set(RedisKit.COOKIE_ID, token);
        } catch (LogException e) {
            LOG.error(e.getMessage(), e);
            return Ret.fail(INTERNAL_EXCEPTION);
        }
    }

    /**
     * 邮箱登录
     *
     * @param email    邮箱
     * @param password 密码
     * @param ip       ip地址
     * @return 返回信息
     */
    Ret loginByEmail(String email, String password, String ip) {
        try {
            String token = SRV.loginByEmail(email, password, ip);
            if (null == token) {
                return Ret.fail("email or password wrong");
            }
            return Ret.succeed().set(RedisKit.COOKIE_ID, token);
        } catch (LogException e) {
            LOG.error(e.getMessage(), e);
            return Ret.fail(INTERNAL_EXCEPTION);
        }
    }

    /**
     * 手机号登录
     *
     * @param phone 手机号
     * @param ip    ip地址
     * @return 返回信息
     */
    Ret loginByPhone(String phone, String ip) {
        try {
            String token = SRV.loginByPhone(phone, ip);
            if (null == token) {
                return Ret.fail("phone or captcha wrong");
            }
            return Ret.succeed().set(RedisKit.COOKIE_ID, token);
        } catch (LogException e) {
            LOG.error(e.getMessage(), e);
            return Ret.fail(INTERNAL_EXCEPTION);
        }
    }

}
