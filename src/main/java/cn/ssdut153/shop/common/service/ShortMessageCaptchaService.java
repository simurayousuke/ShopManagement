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

import cn.ssdut153.shop.common.kit.RedisKit;
import cn.ssdut153.shop.common.kit.ShortMessageKit;

import java.util.Random;

/**
 * The service for Generating and Validating short message captcha.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.6
 * @since 1.0.0
 */
public class ShortMessageCaptchaService {

    /**
     * singleton
     */
    public static final ShortMessageCaptchaService me = new ShortMessageCaptchaService();
    private static String base = "0123456789";

    private ShortMessageCaptchaService() {

    }

    /**
     * set the char set of captcha
     *
     * @param base string
     */
    public static void setBase(String base) {
        ShortMessageCaptchaService.base = base;
    }

    /**
     * generate captcha code with base chars.
     *
     * @param length number of chars of captcha
     * @return captcha code
     */
    private String generateCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * generate a captcha code, save it into redis and send to user's phone.
     *
     * @param number    phone number
     * @param username  username
     * @param operation operation
     */
    public void generateCaptchaAndSend(String number, String username, String operation) {
        String code = generateCode(6);
        RedisKit.setCaptcha(number, code);
        ShortMessageKit.send(username, operation, code, number);
    }

    /**
     * generate a captcha code, save it into redis and send to user's phone.
     *
     * @param number    phone number
     * @param operation operation
     */
    public void generateCaptchaAndSend(String number, String operation) {
        generateCaptchaAndSend(number, "用户", operation);
    }

    /**
     * generate a captcha code, save it into redis and send to user's phone.
     *
     * @param number phone number
     */
    public void generateCaptchaAndSend(String number) {
        generateCaptchaAndSend(number, "用户", "获取验证码");
    }

    /**
     * validate captcha code.
     *
     * @param number  phone number
     * @param captcha input captcha code
     * @return boolean
     */
    public boolean validate(String number, String captcha) {
        if (!captcha.equals(RedisKit.getCaptcha(number))) {
            return false;
        }
        RedisKit.delCaptcha(number);
        return true;
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

}
