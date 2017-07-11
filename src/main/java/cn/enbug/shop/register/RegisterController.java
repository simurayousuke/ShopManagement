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

import cn.enbug.shop.captcha.ImageCaptchaValidator;
import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.Ret;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * 注册
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.4
 * @since 1.0.0
 */
@Before(NoUrlPara.class)
public class RegisterController extends BaseController {

    private static final RegisterService SRV = RegisterService.ME;
    private static final String INDEX_HTML = "index.html";

    /**
     * 注册页面
     */
    @Before(GET.class)
    public void index() {
        render(INDEX_HTML);
    }

    /**
     * 邮箱注册
     */
    @Before({POST.class, ImageCaptchaValidator.class, EmailRegisterValidator.class})
    public void email() {
        String email = getPara("email");
        String ip = getIp();
        Ret ret = SRV.registerByEmail(email, ip);
        renderJson(ret);
    }

    /**
     * 手机号注册
     */
    @Before({POST.class, PhoneRegisterValidator.class})
    public void phone() {
        String phone = getPara("phone");
        String ip = getIp();
        Ret ret = SRV.registerByPhone(phone, ip);
        renderJson(ret);
    }

    @Clear(NoUrlPara.class)
    @Before(GET.class)
    public void step2() {
        String activeCode = getPara();
        if (SRV.handleStep2(activeCode)) {
            render("step2.html");
        } else {
            redirect("/register");
        }
    }

    @Before(POST.class)
    public void step2handler() {

    }

    @Before(GET.class)
    public void success() {
        render("success.html");
    }

}
