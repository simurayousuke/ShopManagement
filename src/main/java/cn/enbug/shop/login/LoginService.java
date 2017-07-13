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
import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.register.RegisterService;

/**
 * 登录服务
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.1.0
 * @since 1.0.0
 */
public class LoginService {

    public static final LoginService me = new LoginService();
    private static final User userDao = new User().dao();

    /**
     * 用户名登录
     *
     * @param username 用户名
     * @param password 密码
     * @param ip       ip地址
     * @return 返回信息
     */
    Ret loginByUsername(String username, String password, String ip) {
        User user = findUserByUsername(username);
        if (null == user) {
            return Ret.fail("username or password wrong");
        }
        String pwd = RegisterService.me.hash(password, user.getSalt());
        if (!pwd.equals(user.getPwd())) {
            return Ret.fail("username or password wrong");
        }
        Log log = new Log().setIp(ip).setOperation("login").setUserId(user.getId()).setDescription("true");
        if (!log.save()) {
            throw new LogException("Can not log login action");
        }
        String token = RedisKit.setAndGetToken(user);
        return Ret.succeed().set(RedisKit.COOKIE_ID, token);
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
        User user = findUserByEmail(email);
        if (null == user) {
            return Ret.fail("wrong email or password");
        }
        String pwd = RegisterService.me.hash(password, user.getSalt());
        if (!pwd.equals(user.getPwd())) {
            return Ret.fail("wrong email or password");
        }
        Log log = new Log().setIp(ip).setOperation("login").setUserId(user.getId()).setDescription("true");
        if (!log.save()) {
            throw new LogException("Can not log email login action");
        }
        String token = RedisKit.setAndGetToken(user);
        return Ret.succeed().set(RedisKit.COOKIE_ID, token);
    }

    /**
     * 手机号登录
     *
     * @param phone 手机号
     * @param ip    ip地址
     * @return 返回信息
     */
    Ret loginByPhone(String phone, String ip) {
        User user = findUserByPhone(phone);
        if (null == user) {
            return Ret.fail("wrong phone or captcha");
        }
        Log log = new Log().setUserId(user.getId()).setIp(ip).setOperation("phoneLogin").setDescription("true");
        if (!log.save()) {
            throw new LogException("Can not log username phoneLogin action");
        }
        String token = RedisKit.setAndGetToken(user);
        return Ret.succeed().set(RedisKit.COOKIE_ID, token);
    }

    /**
     * find user by phone number
     *
     * @param phoneNumber phone number
     * @return User object
     */
    public User findUserByPhone(String phoneNumber) {
        return null == phoneNumber ? null : userDao.findFirst(userDao.getSqlPara("user.findByPhone", phoneNumber));
    }

    public User findUserByUsername(String username) {
        return null == username ? null : userDao.findFirst(userDao.getSqlPara("user.findByUsername", username));
    }

    /**
     * find user by email
     *
     * @param email email
     * @return User object
     */
    public User findUserByEmail(String email) {
        return null == email ? null : userDao.findFirst(userDao.getSqlPara("user.findByEmail", email));
    }

}
