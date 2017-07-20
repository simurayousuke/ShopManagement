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
import cn.enbug.shop.common.exception.LogException;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.EmailService;
import cn.enbug.shop.common.service.ShortMessageCaptchaService;
import cn.enbug.shop.common.service.UserService;
import cn.enbug.shop.login.LoginService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 注册服务
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.1.2
 * @since 1.0.0
 */
public class RegisterService {

    public static final RegisterService me = Duang.duang(RegisterService.class);
    private static final EmailService EMAIL_SRV = EmailService.ME;

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
     * 邮箱注册
     *
     * @param email email
     * @param ip    ip地址
     * @return 结果
     */
    @Before(Tx.class)
    Ret registerByEmail(String email, String ip) {
        User curr = UserService.ME.findUserByEmail(email);
        if (null != curr && curr.getEmailStatus() != 0) {
            return Ret.fail("邮箱已被注册");
        }
        if (null == curr) {
            User user = new User().setUuid(StrKit.getRandomUUID()).setEmail(email).setEmailStatus(0);
            Log log = new Log().setIp(ip).setOperation("initEmail");
            if (!user.save()) {
                return Ret.fail("unable to save into the database");
            }
            if (!log.setUserId(user.getId()).save()) {
                throw new LogException("cannot log email register action");
            }
        }
        if (!sendRegisterEmail(email)) {
            return Ret.fail("fail to send email");
        }
        return Ret.succeed();
    }

    public boolean sendRegisterEmail(String email) {
        String activeCode = RedisKit.setActiveCodeForEmailAndGet(email);
        String title = "激活你的账户";
        String url = "https://shop.yangzhizhuang.net/register/step2/" + activeCode;
        String context = "<a href=\"" + url + "\">点击激活</a>" +
                "<br>若上方链接不可用，您也可以复制地址到浏览器地址栏访问" +
                "<br>" + url;
        return EMAIL_SRV.send(new Email(email, title, context), "register");
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
     * handle register step 2.
     *
     * @param code     active code
     * @param username username
     * @param password password
     * @param ip       ip address
     * @return boolean
     */
    @Before(Tx.class)
    Ret handleStep2(String code, String username, String password, String ip) {
        String number = RedisKit.getPhoneNumberByActiveCode(code);
        if (null != number) {
            return handlePhone(code, ip, username, password, number);
        }
        String email = RedisKit.getEmailAddressByActiveCode(code);
        if (null != email) {
            return handleEmail(code, ip, username, password, email);
        }
        return Ret.fail("invalidated active code.");
    }

    private Ret handleEmail(String code, String ip, String username, String password, String email) {
        User user = LoginService.me.findUserByEmail(email);
        user.setUsername(username).setPwd(password).setEmailStatus(1);
        if (!regUserByEmail(user, ip)) {
            return Ret.fail("unknown error.");
        }
        RedisKit.delActiveCodeForEmail(code);
        return Ret.succeed();
    }

    private Ret handlePhone(String code, String ip, String username, String password, String number) {
        if (!regUserByPhone(ip, username, password, number)) {
            return Ret.fail("unknown error.");
        }
        RedisKit.delActiveCodeForPhoneNumber(code);
        return Ret.succeed();
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
    public boolean regUserByPhone(String ip, String username, String password, String phone) {
        User user = new User();
        user.setUuid(StrKit.getRandomUUID());
        user.setUsername(username).setPwd(password);
        user.setPhone(phone);
        return regUserByPhone(user, ip);
    }

    private boolean regUserByEmail(User user, String ip) {
        user.setSalt(StrKit.getRandomUUID());
        user.setPwd(hash(user.getPwd(), user.getSalt()));
        return user.update() && regUser(user, ip);
    }

    private boolean regUserByPhone(User user, String ip) {
        user.setPwd(hash(user.getPwd(), user.getSalt()));
        return user.save() && regUser(user, ip);
    }

    /**
     * reg user.
     *
     * @param user User Object
     * @param ip   ip address
     * @return boolean
     */
    private boolean regUser(User user, String ip) {
        Log log = new Log().setIp(ip).setOperation("regUser");
        if (!log.setUserId(user.getId()).save()) {
            throw new LogException("cannot log register exception");
        }
        return true;
    }

}
