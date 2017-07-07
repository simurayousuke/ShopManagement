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

import java.util.Random;

/**
 * The service for Generating and Validating short message captcha.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
public class ShortMessageCaptchaService {

    /**
     * singleton
     */
    private static ShortMessageCaptchaService instance=new ShortMessageCaptchaService();

    private ShortMessageCaptchaService(){}

    /**
     * get ShortMessageCaptchaService instance
     *
     * @return singleton
     */
    public static ShortMessageCaptchaService getInstance(){return instance;}

    private static String base = "0123456789";


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
     * set the char set of captcha
     *
     * @param base string
     */
    public void setBase(String base) {
        ShortMessageCaptchaService.base = base;
    }

    /**
     * generate a captcha code and save it into redis
     *
     * @param number phone number
     * @return captcha code
     */
    public String generateCaptcha(String number) {
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
    public boolean validate(String number, String captcha) {
        if (!captcha.equals(RedisKit.getCaptcha(number))) {
            return false;
        }
        RedisKit.delCaptcha(number);
        return true;
    }

}
