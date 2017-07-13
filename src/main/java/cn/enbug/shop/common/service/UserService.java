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
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;

/**
 * The service for user-oriented actions.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.2.11
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

}
