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

package cn.ssdut153.shop.common.kit;

import cn.ssdut153.shop.common.model.User;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Redis;

/**
 * The help kit for Redis.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.3
 * @since 1.0.0
 */
public class RedisKit {

    /**
     * the key in cookie of token.
     */
    public static final String COOKIE_ID = "token";
    private static final String TOKEN = "token";
    private static final String SHORT_MESSAGE_CAPTCHA = "shortMessageCaptcha";

    private RedisKit() {

    }

    /**
     * set token and return its value.
     *
     * @param user User Object
     * @return token
     */
    public static String setAndGetToken(User user) {
        String token = StrKit.getRandomUUID();
        setToken(user, token);
        return token;
    }

    /**
     * get User Object by token.
     *
     * @param token token
     * @return User Object or null
     */
    public static User getUserByToken(String token) {
        return null == token ? null : Redis.use(TOKEN).get(token);
    }

    /**
     * set token for User.
     *
     * @param user  User object
     * @param token token
     */
    public static void setToken(User user, String token) {
        Redis.use(TOKEN).setex(token, 3600, user);
    }

    /**
     * set captcha code into redis.
     *
     * @param number phone number
     * @param code   captcha code
     */
    public static void setCaptcha(String number, String code) {
        Redis.use(SHORT_MESSAGE_CAPTCHA).setex(number, 15 * 60, code);
    }

    /**
     * get captcha code from redis.
     *
     * @param number number
     * @return captcha code
     */
    public static String getCaptcha(String number) {
        return null == number ? null : Redis.use(SHORT_MESSAGE_CAPTCHA).get(number);
    }

    /**
     * delete captcha by phone number.
     *
     * @param number phone number
     */
    public static void delCaptcha(String number) {
        Redis.use(SHORT_MESSAGE_CAPTCHA).del(number);
    }

}
