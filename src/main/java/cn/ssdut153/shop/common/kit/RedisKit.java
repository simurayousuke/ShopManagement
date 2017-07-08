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
 * @version 1.0.8
 * @since 1.0.0
 */
public class RedisKit {

    /**
     * the key in cookie of token.
     */
    public static final String COOKIE_ID = "token";
    public static final String TOKEN = "token";
    public static final String SHORT_MESSAGE_CAPTCHA = "shortMessageCaptcha";
    public static final String ACTIVE_CODE_FOR_PHONE_NUMER = "activeCodeForPhoneNumber";
    public static final String ACTIVE_CODE_FOR_EMAIL = "activeCodeForEmail";

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

    /**
     * set active code for phone number.
     *
     * @param number phone number
     * @param code   active code(uuid)
     */
    public static void setActiveCodeForPhoneNumber(String number, String code) {
        Redis.use(ACTIVE_CODE_FOR_PHONE_NUMER).setex(code, 15 * 60, number);
    }

    /**
     * set and get active code for phone number.
     *
     * @param number phone number
     * @return active code
     */
    public static String setActiveCodeForPhoneNumberAndGet(String number) {
        String code=StrKit.getRandomUUID();
        Redis.use(ACTIVE_CODE_FOR_PHONE_NUMER).setex(code, 15 * 60, number);
        return code;
    }

    /**
     * get phone number by active code.
     *
     * @param code active code
     * @return phone number or null
     */
    public static String getPhoneNumberByActiveCode(String code) {
        return null == code ? null : Redis.use(ACTIVE_CODE_FOR_PHONE_NUMER).get(code);
    }

    /**
     * delete active code for phone number.
     *
     * @param code active code
     */
    public static void delActiveCodeForPhoneNumber(String code) {
        Redis.use(ACTIVE_CODE_FOR_PHONE_NUMER).del(code);
    }

    /**
     * set and get active code for email.
     *
     * @param emailAddress email address
     * @return active code
     */
    public static String setActiveCodeForEmailAndGet(String emailAddress){
        String code=StrKit.getRandomUUID();
        Redis.use(ACTIVE_CODE_FOR_EMAIL).setex(code,2*60*60,emailAddress);
        return code;
    }

    /**
     * get email address by active code
     *
     * @param code active code
     * @return email address
     */
    public static String getEmailAddressByActiveCode(String code){
        return null==code?null:Redis.use(ACTIVE_CODE_FOR_EMAIL).get(code);
    }

    /**
     * delete active code for email.
     *
     * @param code active code
     */
    public static void delActiveCodeForEmail(String code){
        Redis.use(ACTIVE_CODE_FOR_EMAIL).del(code);
    }

}
