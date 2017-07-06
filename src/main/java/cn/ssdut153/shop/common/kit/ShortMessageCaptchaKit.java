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
 *  limitations under the License.
 */

package cn.ssdut153.shop.common.kit;

import java.util.Random;

/**
 * The help kit for Generating and Validating short message captcha.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class ShortMessageCaptchaKit {

    private static String base = "0123456789";

    private ShortMessageCaptchaKit() {

    }

    /**
     * generate captcha code with base chars.
     *
     * @param length number of chars of captcha
     * @return captcha code
     */
    private static String generateCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * set the char set of captcha
     *
     * @param base string
     */
    public static void setBase(String base) {
        ShortMessageCaptchaKit.base = base;
    }

    /**
     * generate a captcha code and save it into redis
     *
     * @param number phone number
     * @return captcha code
     */
    public static String generateCaptcha(String number) {
        String code = generateCode(6);
        RedisKit.setCaptcha(number, code);
        return code;
    }

    /**
     * validate captcha code.
     *
     * @param number  phone number
     * @param captcha input captcha code
     * @return boolean
     */
    public static boolean validate(String number, String captcha) {
        if (!captcha.equals(RedisKit.getCaptcha(number))) {
            return false;
        }
        RedisKit.delCaptcha(number);
        return true;
    }

}
