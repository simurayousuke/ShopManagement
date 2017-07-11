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

package cn.enbug.shop.register;

import cn.enbug.shop.common.bean.Email;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.EmailService;
import cn.enbug.shop.common.service.ShortMessageCaptchaService;
import cn.enbug.shop.common.service.UserService;

/**
 * 注册服务
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.6
 * @since 1.0.0
 */
class RegisterService {

    static final RegisterService ME = new RegisterService();
    private static final UserService USER_SRV = UserService.ME;
    private static final EmailService EMAIL_SRV = EmailService.ME;

    /**
     * 邮箱注册
     *
     * @param email email
     * @param ip    ip地址
     * @return 结果
     */
    Ret registerByEmail(String email, String ip) {
        boolean b = USER_SRV.initUserByEmail(email, ip);
        if (!b) {
            return Ret.fail();
        }
        String activeCode = EMAIL_SRV.generateActiveCodeForEmail(email);
        String title = "激活你的账户";
        String context = "<a href=\"https://shop.yangzhizhuang.net/register/step2/" + activeCode +
                "\">点击激活</a><br>若上方链接不可用，您也可以复制地址到浏览器地址栏访问<br>" +
                "https://shop.yangzhizhuang.net/register/step2/" + activeCode;
        if (EMAIL_SRV.send(new Email(email, title, context), "register")) {
            return Ret.succeed();
        }
        return Ret.fail();
    }

    /**
     * 手机号注册
     *
     * @param phone 手机号
     * @param ip    ip地址
     * @return 结果
     */
    Ret registerByPhone(String phone, String ip) {
        String activeCode = ShortMessageCaptchaService.ME.generateActiveCodeForPhoneNumberAndGet(phone);
        return Ret.succeed().set("activeCode", activeCode);
    }

    /**
     * validate register step 2.
     *
     * @param code active code
     * @return boolean
     */
    boolean validateStep2(String code) {
        return ShortMessageCaptchaService.ME.validateActiveCodeForPhoneNumber(code) ||
                EMAIL_SRV.validateActiveCodeForEmail(code);
    }

    /**
     * handle register step 2.
     *
     * @param code     active code
     * @param username username
     * @param password password
     * @param ip       ip address
     * @return boolean
     */
    Ret handleStep2(String code, String username, String password, String ip) {
        if(null != UserService.ME.findUserByUsername(username)){
            return Ret.fail("User already exists.");
        }
        String number = RedisKit.getPhoneNumberByActiveCode(code);
        if (null != number) {
            if(UserService.ME.regUserByPhoneNumber(ip, username, password, number)){
                RedisKit.delActiveCodeForPhoneNumber(code);
                return Ret.succeed();
            }
            return Ret.fail("unknown error.");
        }
        String email = RedisKit.getEmailAddressByActiveCode(code);
        if (null != email) {
            if(UserService.ME.regUserByEmail(ip, username, password, email)){
                RedisKit.delActiveCodeForEmail(code);
                return Ret.succeed();
            }
            return Ret.fail("unknown error.");
        }
        return Ret.fail("invalidated active code.");
    }

}
