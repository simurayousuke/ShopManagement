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

import cn.enbug.shop.common.exception.MoneyTransferException;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;

/**
 * The service for user-oriented actions.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.2.16
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
    public String hash(String password, String salt) {
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
     * find user by id
     *
     * @param id id
     * @return User object
     */
    public User findUserById(int id) {
        return USER_DAO.findFirst(USER_DAO.getSqlPara("user.findById", id));
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
     * validate token.
     *
     * @param token token
     * @return User Object or null
     */
    public User validateToken(String token) {
        return RedisKit.getUserByToken(token);
    }

    /**
     * charge money
     *
     * @param token token
     * @param value value
     * @return boolean
     */
    public boolean charge(String token, BigDecimal value) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        user.setMoney(user.getMoney().add(value));
        return user.update();
    }

    /**
     * transfer money
     *
     * @param token  token
     * @param userId target user id
     * @param value  value
     * @return boolean
     */
    @Before(Tx.class)
    public boolean transfer(String token, int userId, BigDecimal value) {
        User to = findUserById(userId);
        if (to == null) {
            return false;
        }
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        BigDecimal money = user.getMoney();
        if (money.compareTo(value) < 0) {
            return false;
        }
        user.setMoney(user.getMoney().subtract(value));
        to.setMoney(to.getMoney().add(value));
        if (!user.update()) {
            return false;
        }
        if (!to.update()) {
            throw new MoneyTransferException("Fail to transfer money.");
        }
        return true;
    }

    /**
     * transfer money
     *
     * @param user  user
     * @param to    target user
     * @param value value
     * @return boolean
     */
    @Before(Tx.class)
    public boolean transfer(User user, User to, BigDecimal value) {
        BigDecimal money = user.getMoney();
        if (money.compareTo(value) < 0) {
            return false;
        }
        user.setMoney(user.getMoney().subtract(value));
        to.setMoney(to.getMoney().add(value));
        if (!user.update()) {
            return false;
        }
        if (!to.update()) {
            throw new MoneyTransferException("Fail to transfer money.");
        }
        return true;
    }

    @Before(Tx.class)
    public boolean transfer(User user, int userId, BigDecimal value) {

        BigDecimal money = user.getMoney();
        if (money.compareTo(value) < 0) {
            return false;
        }
        user.setMoney(money.subtract(value));
        if (!user.update()) {
            return false;
        }

        User to = findUserById(userId);
        if (null == to) {
            return false;
        }
        to.setMoney(to.getMoney().add(value));
        if (!to.update()) {
            throw new MoneyTransferException("Fail to transfer money.");
        }
        return true;
    }

    /**
     * get money
     *
     * @param token token
     * @return BigDecimal
     */
    public BigDecimal getMoney(String token) {
        User user = RedisKit.getUserByToken(token);
        return null == token ? new BigDecimal(0) : user.getMoney();
    }

    /**
     * change password.
     *
     * @param old old password
     * @param pwd new password
     * @return boolean
     */
    public boolean changePassword(String token, String old, String pwd) {
        User user = RedisKit.getUserByToken(token);
        return null != user && hash(old, user.getSalt()).equals(user.getPwd()) && user.setPwd(hash(pwd, user.getSalt())).update();
    }

    /**
     * resetPasswordByPhone
     *
     * @param activeCode active code
     * @param pwd        new password
     * @return boolean
     */
    public boolean resetPasswordByPhone(String activeCode, String pwd) {
        String phone = RedisKit.getPhoneNumberByActiveCode(activeCode);
        User user = findUserByPhoneNumber(phone);
        return null != user && user.setPwd(hash(pwd, user.getSalt())).update();
    }

    /**
     * resetPasswordByEmail
     *
     * @param activeCode active code
     * @param pwd        new password
     * @return boolean
     */
    public boolean resetPasswordByEmail(String activeCode, String pwd) {
        String email = RedisKit.getEmailAddressByActiveCode(activeCode);
        User user = findUserByEmail(email);
        return null != user && user.setPwd(hash(pwd, user.getSalt())).update();
    }

    /**
     * resetPassword
     *
     * @param activeCode active code
     * @param pwd        password
     * @return boolean
     */
    public boolean resetPassword(String activeCode, String pwd) {
        User user;
        String phone = RedisKit.getPhoneNumberByActiveCode(activeCode);
        if (null == phone) {
            String email = RedisKit.getEmailAddressByActiveCode(activeCode);
            user = findUserByEmail(email);
        } else {
            user = findUserByPhoneNumber(phone);
        }
        return null != user && user.setPwd(hash(pwd, user.getSalt())).update();
    }

    public User getByActiveCode(String activeCode) {
        User user;
        String phone = RedisKit.getPhoneNumberByActiveCode(activeCode);
        if (null == phone) {
            String email = RedisKit.getEmailAddressByActiveCode(activeCode);
            user = findUserByEmail(email);
        } else {
            user = findUserByPhoneNumber(phone);
        }
        return user;
    }

}
