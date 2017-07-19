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

package cn.enbug.shop.forget;

import cn.enbug.shop.common.bean.Email;
import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.EmailService;
import cn.enbug.shop.common.service.UserService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
@Before(NoUrlPara.class)
public class ForgetController extends BaseController {

    @Before(GET.class)
    public void index() {
        render("forgetPassword.html");
    }

    @Clear(NoUrlPara.class)
    @Before(GET.class)
    public void reset() {
        String code = getPara();
        User user = UserService.ME.getByActiveCode(code);
        if (null == user) {
            redirect("/login");
        } else {
            setAttr("code", code);
            setAttr("user", user);
            render("newPassword.html");
        }
    }

    @Before({POST.class, DoResetValidator.class})
    public void doreset() {
        String code = getPara("code");
        String pwd = getPara("pwd");
        if (UserService.ME.resetPassword(code, pwd)) {
            renderJson(Ret.succeed());
        } else {
            renderJson(Ret.fail("修改失败"));
        }
    }

    @Before({POST.class, PhoneValidator.class})
    public void validatePhone() {
        String phone = getPara("phone");
        renderJson(Ret.succeed().set("code", RedisKit.setActiveCodeForPhoneNumberAndGet(phone)));
    }

    @Before({POST.class, EmailValidator.class})
    public void validateEmail() {
        String email = getPara("email");
        String activeCode = RedisKit.setActiveCodeForEmailAndGet(email);
        String title = "您正在找回密码";
        String url = "https://shop.yangzhizhuang.net/forget/reset/" + activeCode;
        String context = "您正在EnBug购物网找回密码<a href=\"" + url + "\">点击重设密码</a>" +
                "<br>若上方链接不可用，您也可以复制地址到浏览器地址栏访问" +
                "<br>" + url;
        if (EmailService.ME.send(new Email(email, title, context), "forget")) {
            renderJson(Ret.succeed());
        } else {
            renderJson(Ret.fail("邮件发送失败"));
        }
    }

}
