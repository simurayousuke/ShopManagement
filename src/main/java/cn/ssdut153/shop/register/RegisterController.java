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

package cn.ssdut153.shop.register;

import cn.ssdut153.shop.common.controller.BaseController;
import cn.ssdut153.shop.common.kit.IpKit;
import cn.ssdut153.shop.common.kit.Ret;
import cn.ssdut153.shop.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * 注册
 *
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({NoUrlPara.class})
public class RegisterController extends BaseController {

    private static final RegisterService srv = RegisterService.me;

    /**
     * 注册页面
     */
    @Before({GET.class})
    public void index() {
        render("index.html");
    }

    /**
     * 邮箱注册
     */
    @Before({POST.class, EmailRegisterValidator.class})
    public void email() {
        User user = getModel(User.class, "");
        String email = getPara("email");
        String ip = IpKit.getRealIp(getRequest());
        Ret ret = srv.registerByEmail(user, email, ip);
        renderJson(ret);
    }

    /**
     * 手机号注册
     */
    @Before({POST.class, PhoneRegisterValidator.class})
    public void phone() {
        User user = getModel(User.class, "");
        String ip = IpKit.getRealIp(getRequest());
        Ret ret = srv.registerByPhone(user, ip);
        renderJson(ret);
    }

}
