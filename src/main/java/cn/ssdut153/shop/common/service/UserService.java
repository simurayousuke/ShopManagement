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

package cn.ssdut153.shop.common.service;

import cn.ssdut153.shop.common.exception.LogException;
import cn.ssdut153.shop.common.kit.RedisKit;
import cn.ssdut153.shop.common.model.Log;
import cn.ssdut153.shop.common.model.User;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * The service for user-oriented actions.
 *
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
public class UserService {

    private static final User userDao = new User().dao();
    private static UserService instance = Duang.duang(UserService.class);

    private UserService(){}

    /**
     * get UserService instance
     *
     * @return singleton
     */
    public static UserService getInstance() {
        return instance;
    }

    /**
     * get hashed password.
     *
     * @param password password
     * @param salt     salt
     * @return hashed password
     */
    private String hash(String password, String salt) {
        String ret = HashKit.sha256(password + salt);
        for (int i = 0; i < 2; i++) {
            ret = HashKit.sha256(ret + salt);
        }
        return ret;
    }

    /**
     * find user by username
     *
     * @param username username
     * @return User object
     */
    public User findUserByUsername(String username) {
        return userDao.findFirst(userDao.getSqlPara("user.findByUsername", username));
    }

    /**
     * find user by phone number
     *
     * @param phoneNumber phone number
     * @return User object
     */
    public User findUserByPhoneNumber(String phoneNumber) {
        return userDao.findFirst(userDao.getSqlPara("user.findByPhoneNumber", phoneNumber));
    }

    /**
     * register a user and log the action.
     *
     * @param user User object
     * @param ip   ip address
     * @return success or not
     */
    public boolean register(User user, String ip) {
        user.setSalt(StrKit.getRandomUUID());
        user.setPwd(hash(user.getPwd(), user.getSalt()));
        return Db.tx(4, () -> user.save() && new Log().setIp(ip).setOperation("register").setUserId(user.getId()).save());
    }

    /**
     * Validate user with username&password.
     *
     * @param username username
     * @param password password
     * @param ip       ip address
     * @return token or null
     */
    public String login(String username, String password, String ip) {
        User user = findUserByUsername(username);
        if (null == user) {
            return null;
        }
        Boolean status = hash(password, user.getSalt()).equals(user.getPwd());
        if (!new Log().setIp(ip).setOperation("login").setUserId(user.getId()).setDescription(status.toString()).save()) {
            throw new LogException("Can not log login action");
        }
        return status? RedisKit.setAndGetToken(user):null;
    }

    /**
     * validate user with phone number&captcha.
     *
     * @param phoneNumber phone number
     * @param captcha     captcha code
     * @param ip          ip address
     * @return token or null
     */
    public String loginByPhone(String phoneNumber, String captcha, String ip) {
        Boolean status = ShortMessageCaptchaService.validate(phoneNumber, captcha);
        if (!new Log().setUserId(findUserByPhoneNumber(phoneNumber)
                .getId()).setIp(ip).setOperation("phoneLogin").setDescription(status.toString()).save()) {
            throw new LogException("Can not log phoneLogin action");
        }
        User user=findUserByPhoneNumber(phoneNumber);
        return status?RedisKit.setAndGetToken(user):null;
    }

    /**
     * validate token.
     *
     * @param token token
     * @return User Object or null
     */
    public User validateToken(String token){
        return RedisKit.getUserByToken(token);
    }

}
