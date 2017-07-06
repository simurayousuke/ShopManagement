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

import com.jfinal.plugin.redis.Redis;

/**
 * The help kit for Redis.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class RedisKit {

    /**
     * the key in cookie of token.
     */
    public static String COOKIE_ID = "token";

    // todo 返回User
    public static String getUsernameByToken(String token) {
        return null == token ? null : Redis.use("token").get(token);
    }

    // todo 设置User
    public static void setToken(String username, String token) {
        Redis.use("token").setex(token, 3600, username);
    }

    /**
     * set captcha code into redis
     *
     * @param number phone number
     * @param code   captcha code
     */
    public static void setCaptcha(String number, String code) {
        Redis.use("shortMessageCaptcha").setex(number, 15 * 60, code);
    }

    /**
     * get captcha code from redis
     *
     * @param number number
     * @return captcha code
     */
    public static String getCaptcha(String number) {
        return null == number ? null : Redis.use("shortMessageCaptcha").get(number);
    }

    public static void delCaptcha(String number) {
        Redis.use("shortMessageCaptcha").del(number);
    }

}
