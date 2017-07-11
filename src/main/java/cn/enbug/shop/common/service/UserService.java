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

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.exception.LogException;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * The service for user-oriented actions.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.2.8
 * @since 1.0.0
 */
public class UserService {

    public static final UserService ME = Duang.duang(UserService.class);
    private static final User USER_DAO = new User().dao();

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
        if (null == username) {
            return null;
        }
        return USER_DAO.findFirst(USER_DAO.getSqlPara("user.findByUsername", username));
    }

    /**
     * find user by phone number
     *
     * @param phoneNumber phone number
     * @return User object
     */
    public User findUserByPhoneNumber(String phoneNumber) {
        if (null == phoneNumber) {
            return null;
        }
        return USER_DAO.findFirst(USER_DAO.getSqlPara("user.findByPhoneNumber", phoneNumber));
    }

    /**
     * find user by email
     *
     * @param email email
     * @return User object
     */
    public User findUserByEmail(String email) {
        if (null == email) {
            return null;
        }
        return USER_DAO.findFirst(USER_DAO.getSqlPara("user.findByEmail", email));
    }

    /**
     * register a user and log the action.
     *
     * @param user User object
     * @param ip   ip address
     * @return success or not
     * @deprecated
     */
    @Deprecated
    public boolean register(User user, String ip) {
        user.setSalt(StrKit.getRandomUUID());
        user.setPwd(hash(user.getPwd(), user.getSalt()));
        return Db.tx(4, () -> user.save() &&
                new Log().setIp(ip).setOperation("register").setUserId(user.getId()).save());
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
        if (!new Log().setIp(ip).setOperation("login")
                .setUserId(user.getId()).setDescription(status.toString()).save()) {
            throw new LogException("Can not log login action");
        }
        return status ? RedisKit.setAndGetToken(user) : null;
    }

    /**
     * validate user with phone number&captcha.
     *
     * @param phoneNumber phone number
     * @param captcha     captcha code
     * @param ip          ip address
     * @return token or null
     * @throws LogException if save to log fail
     */
    public String loginByPhone(String phoneNumber, String captcha, String ip) {
        User user = findUserByPhoneNumber(phoneNumber);
        if (null == user) {
            return null;
        }
        Boolean status = ShortMessageCaptchaService.ME.validate(phoneNumber, captcha);
        if (!new Log().setUserId(user.getId())
                .setIp(ip).setOperation("phoneLogin").setDescription(status.toString()).save()) {
            throw new LogException("Can not log username phoneLogin action");
        }
        return status ? RedisKit.setAndGetToken(user) : null;
    }

    /**
     * should not be invoked unless the user is validated by the validator.
     *
     * @param phoneNumber phone number
     * @param ip          ip address
     * @return token
     */
    public String loginByPhone(String phoneNumber, String ip) {
        User user = findUserByPhoneNumber(phoneNumber);
        if (null == user) {
            return null;
        }
        if (!new Log().setUserId(user.getId())
                .setIp(ip).setOperation("phoneLogin").setDescription("true").save()) {
            throw new LogException("Can not log username phoneLogin action");
        }
        return RedisKit.setAndGetToken(user);
    }

    /**
     * validate user with email&password
     *
     * @param email    email
     * @param password password
     * @param ip       ip address
     * @return token or null
     * @throws LogException if save to log fail
     */
    public String loginByEmail(String email, String password, String ip) {
        User user = findUserByEmail(email);
        if (null == user) {
            return null;
        }
        Boolean status = hash(password, user.getSalt()).equals(user.getPwd());
        if (!new Log().setIp(ip).setOperation("login")
                .setUserId(user.getId()).setDescription(status.toString()).save()) {
            throw new LogException("Can not log email login action");
        }
        return status ? RedisKit.setAndGetToken(user) : null;
    }

    /**
     * validate token.
     *
     * @param token token
     * @return User Object or null
     */
    public User validateToken(String token) {
        return RedisKit.getUserByToken(token);
    }

    /**
     * init a user with uuid.
     *
     * @return User Object
     */
    private User initUser() {
        User user = new User();
        user.setUuid(StrKit.getRandomUUID()).save();
        return user;
    }

    private User initUserWithoutSave() {
        User user = new User();
        user.setUuid(StrKit.getRandomUUID());
        return user;
    }

    /**
     * init user with phone number.
     *
     * @param number phone number
     * @param ip     ip address
     * @return boolean
     */
    public boolean initUserByPhoneNumber(String number, String ip) {
        if (null != findUserByPhoneNumber(number)) {
            return false;
        }
        User user = initUser();
        new Log().setIp(ip).setOperation("initPhone").setUserId(user.getId()).save();
        return ShortMessageCaptchaService.ME.bindPhoneNumberForUser(user, number);
    }

    /**
     * init user with email.
     *
     * @param emailAddress email address
     * @param ip           ip address
     * @return boolean
     */
    public boolean initUserByEmail(String emailAddress, String ip) {
        User select = findUserByEmail(emailAddress);
        if (null != select) {
            return 0 == select.getEmailStatus();
        }
        User user = new User();
        user.setUuid(StrKit.getRandomUUID());
        user.setEmail(emailAddress);
        user.setEmailStatus(0);
        return Db.tx(() -> user.save() && new Log().setIp(ip).setOperation("initEmail").setUserId(user.getId()).save());
    }

    /**
     * reg user.
     *
     * @param user User Object
     * @param ip   ip address
     * @return boolean
     */
    private boolean regUser(User user, String ip) {
        user.setSalt(StrKit.getRandomUUID());
        user.setPwd(hash(user.getPwd(), user.getSalt()));
        return Db.tx(4, () -> user.update() &&
                new Log().setIp(ip).setOperation("regUser").setUserId(user.getId()).save());
    }

    /**
     * reg user.
     *
     * @param ip       ip address
     * @param username username
     * @param password password
     * @param number   phone number
     * @return boolean
     */
    public boolean regUserByPhoneNumber(String ip, String username, String password, String number) {
        if(!initUserByPhoneNumber(number,ip)){
            return false;
        }
        User user = findUserByPhoneNumber(number).setUsername(username).setPwd(password);
        return regUser(user, ip);
    }

    /**
     * reg user.
     *
     * @param ip           ip address
     * @param username     username
     * @param password     password
     * @param emailAddress email address
     * @return boolean
     */
    public boolean regUserByEmail(String ip, String username, String password, String emailAddress) {
        User user = findUserByEmail(emailAddress).setUsername(username).setPwd(password);
        return regUser(user, ip);
    }

}
