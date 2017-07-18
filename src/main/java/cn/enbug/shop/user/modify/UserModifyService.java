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

package cn.enbug.shop.user.modify;

import cn.enbug.shop.common.bean.Email;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.AddressService;
import cn.enbug.shop.common.service.EmailService;
import cn.enbug.shop.common.service.UserService;

import java.math.BigDecimal;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.7
 * @since 1.0.0
 */
public class UserModifyService {

    public static final UserModifyService ME = new UserModifyService();

    private boolean sendBindEmail(String email, User user) {
        String activeCode = RedisKit.setActiveCodeForEmailAndGet(email);
        RedisKit.setUserForActiveCode(activeCode, user);
        String title = "您正在绑定邮箱";
        String url = "https://shop.yangzhizhuang.net/user/modify/active/" + activeCode;
        String context = "您正在EnBug购物网绑定邮箱，<a href=\"" + url + "\">点击绑定</a>" +
                "<br>若上方链接不可用，您也可以复制地址到浏览器地址栏访问" +
                "<br>" + url;
        return EmailService.ME.send(new Email(email, title, context), "bind");
    }

    Ret setAvator(String token, String avator) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return Ret.fail("登录超时");
        }
        user.setAvator(avator);
        return user.update() ? Ret.succeed() : Ret.fail("设置失败");
    }

    Ret addAddress(String token, String name, String phone, String address) {
        return AddressService.ME.insert(token, name, phone, address) ? Ret.succeed() : Ret.fail("设置失败");
    }

    Ret charge(String token, BigDecimal value) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return Ret.fail("登录超时！");
        }
        if ("ys727469926".equals(user.getUsername())) {
            return Ret.fail("当前系统设置禁止杨森充值");
        }
        user.setMoney(user.getMoney().add(value));
        return user.update() ? Ret.succeed() : Ret.fail("充值失败！");
    }

    Ret setDefaultAddress(String token, int id) {
        return AddressService.ME.setDefault(token, id) ? Ret.succeed() : Ret.fail("设置失败！");
    }

    Ret bindPhone(String token, String number, String captcha) {
        User curr = UserService.ME.findUserByPhoneNumber(number);
        if (curr != null) {
            return Ret.fail("手机号已被使用");
        }
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return Ret.fail("登录超时");
        }
        return user.setPhone(number).update() ? Ret.succeed() : Ret.fail("设置失败");
    }

    Ret bindEmail(String token, String email) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return Ret.fail("登录超时");
        }
        if (sendBindEmail(email, user)) {
            return Ret.succeed();
        } else {
            return Ret.fail("邮件发送失败");
        }
    }

    boolean activeEmail(String code) {
        User user = RedisKit.getUserFromActiveCode(code);
        String email = RedisKit.getEmailAddressByActiveCode(code);
        if (null == user || null == email) {
            return false;
        }
        User curr = UserService.ME.findUserByEmail(email);
        if (curr != null) {
            if (curr.getEmailStatus() != 0) {
                return false;
            } else {
                curr.delete();
            }
        }
        if (user.setEmail(email).setEmailStatus(1).update()) {
            RedisKit.delActiveCodeForEmail(code);
            RedisKit.delUserForActiveCode(code);
            return true;
        }
        return false;
    }

}
