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

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.login.LoginService;
import cn.enbug.shop.register.RegisterService;
import com.jfinal.aop.Duang;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * The service for user-oriented actions.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.2.10
 * @since 1.0.0
 */
public class UserService {

    public static final UserService ME = Duang.duang(UserService.class);

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
        user.setPwd(RegisterService.me.hash(user.getPwd(), user.getSalt()));
        return Db.tx(4, () -> user.save() &&
                new Log().setIp(ip).setOperation("register").setUserId(user.getId()).save());
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
     * @param phone phone number
     * @param ip    ip address
     * @return boolean
     */
    public boolean initUserByPhoneNumber(String phone, String ip) {
        if (null != LoginService.me.findUserByPhone(phone)) {
            return false;
        }
        User user = initUser();
        new Log().setIp(ip).setOperation("initPhone").setUserId(user.getId()).save();
        return ShortMessageCaptchaService.ME.bindPhoneNumberForUser(user, phone);
    }

    /**
     * init user with email.
     *
     * @param email email address
     * @param ip    ip address
     * @return boolean
     */
    public boolean initUserByEmail(String email, String ip) {
        User select = LoginService.me.findUserByEmail(email);
        if (null != select) {
            return 0 == select.getEmailStatus();
        }
        User user = new User();
        user.setUuid(StrKit.getRandomUUID());
        user.setEmail(email);
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
        user.setPwd(RegisterService.me.hash(user.getPwd(), user.getSalt()));
        return Db.tx(4, () -> user.update() &&
                new Log().setIp(ip).setOperation("regUser").setUserId(user.getId()).save());
    }

    /**
     * reg user.
     *
     * @param ip       ip address
     * @param username username
     * @param password password
     * @param phone    phone number
     * @return boolean
     */
    public boolean regUserByPhoneNumber(String ip, String username, String password, String phone) {
        if (!initUserByPhoneNumber(phone, ip)) {
            return false;
        }
        User user = LoginService.me.findUserByPhone(phone).setUsername(username).setPwd(password);
        return regUser(user, ip);
    }

    /**
     * reg user.
     *
     * @param ip       ip address
     * @param username username
     * @param password password
     * @param email    email address
     * @return boolean
     */
    public boolean regUserByEmail(String ip, String username, String password, String email) {
        User user = LoginService.me.findUserByEmail(email).setUsername(username).setPwd(password).setEmailStatus(1);
        return regUser(user, ip);
    }

}
