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
 * @version 1.2.1
 * @since 1.0.0
 */
public class UserService {

    private static final User userDao = new User().dao();
    private static UserService instance = Duang.duang(UserService.class);

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
     * find user by email
     *
     * @param email email
     * @return User object
     */
    public User findUserByEmail(String email) {
        return userDao.findFirst(userDao.getSqlPara("user.findByEmail", email));
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
        Boolean status = ShortMessageCaptchaService.me.validate(phoneNumber, captcha);
        if (!new Log().setUserId(findUserByPhoneNumber(phoneNumber)
                .getId()).setIp(ip).setOperation("phoneLogin").setDescription(status.toString()).save()) {
            throw new LogException("Can not log username phoneLogin action");
        }
        User user = findUserByPhoneNumber(phoneNumber);
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
        if (!new Log().setUserId(findUserByPhoneNumber(phoneNumber)
                .getId()).setIp(ip).setOperation("phoneLogin").setDescription("true").save()) {
            throw new LogException("Can not log username phoneLogin action");
        }
        User user = findUserByPhoneNumber(phoneNumber);
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
        if (!new Log().setIp(ip).setOperation("login").setUserId(user.getId()).setDescription(status.toString()).save()) {
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

    // todo 记录操作到数据库中

    /**
     * generate and get active code for phone number.
     *
     * @param number phone number
     * @return active code
     */
    public String generateActiveCodeForPhoneNumberAndGet(String number) {
        return RedisKit.setActiveCodeForPhoneNumberAndGet(number);
    }

    /**
     * match phone number with active code.
     *
     * @param number phone number
     * @param code   active code
     * @return boolean
     */
    public boolean validateActiveCodeWithPhoneNumber(String number, String code) {
        return number.equals(RedisKit.getPhoneNumberByActiveCode(code));
    }

    /**
     * validate active code.
     *
     * @param code active code
     * @return boolean
     */
    public boolean validateActiveCodeForPhoneNumber(String code) {
        return RedisKit.getPhoneNumberByActiveCode(code) != null;
    }

    /**
     * generate and get active code for email.
     *
     * @param emailAddress email address
     * @return active code
     */
    public String generateActiveCodeForEmail(String emailAddress) {
        return RedisKit.setActiveCodeForEmailAndGet(emailAddress);
    }

    /**
     * validate active code with email.
     *
     * @param emailAddress email address
     * @param code         active code
     * @return boolean
     */
    public boolean validateActiveCodeWithEmail(String emailAddress, String code) {
        return emailAddress.equals(RedisKit.getEmailAddressByActiveCode(code));
    }

    /**
     * validate active code for email.
     *
     * @param code active code
     * @return boolean
     */
    public boolean validateActiveCodeForEmail(String code) {
        return RedisKit.getEmailAddressByActiveCode(code) != null;
    }

    /**
     * bind email address for user.
     *
     * @param user         User Object
     * @param emailAddress email address
     * @return boolean
     */
    public boolean bindEmailAddressForUser(User user, String emailAddress) {
        return user.setEmail(emailAddress).setEmailStatus(0).update();
    }

    /**
     * bind email address for username.
     *
     * @param username     username
     * @param emailAddress email address
     * @return boolean
     */
    public boolean bindEmailAddressForUsername(String username, String emailAddress) {
        return bindEmailAddressForUser(findUserByUsername(username), emailAddress);
    }

    /**
     * active email address for user
     *
     * @param user User Object
     * @return boolean
     */
    private boolean activeEmailAddressForUser(User user) {
        if (StrKit.isBlank(user.getEmail())) {
            return false;
        }
        return user.setEmailStatus(1).update();
    }

    /**
     * validate and then active email address for user
     *
     * @param user User Object
     * @param code active code
     * @return boolean
     */
    public boolean validateThenActiveEmailAddressForUser(User user, String code) {
        if (!validateActiveCodeForEmail(code)) {
            return false;
        }
        return activeEmailAddressForUser(user);
    }

    /**
     * active email address for username
     *
     * @param username username
     * @return boolean
     */
    private boolean activeEmailAddressForUsername(String username) {
        return activeEmailAddressForUser(findUserByUsername(username));
    }

    /**
     * validate then active email address for username
     *
     * @param username username
     * @param code     active code
     * @return boolean
     */
    public boolean validateThenActiveEmailAddressForUsername(String username, String code) {
        return validateThenActiveEmailAddressForUser(findUserByUsername(username), code);
    }

}
