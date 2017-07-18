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

package cn.enbug.shop.user.center;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Forrest Yang
 * @author Yang Zhizhuang
 * @author Forrest Yang
 * @version 1.0.4
 * @since 1.0.0
 */
@Before({GET.class, NeedLogInInterceptor.class})
public class UserCenterController extends BaseController {

    public void index() {
        render("secure.html");
    }

    public void userinfo() {
        render("userInfo.html");
    }

    public void recharge() {
        render("recharge.html");
    }

    public void bindphone() {
        render("bindPhone.html");
    }

    public void bindemail() {
        render("bindEmail.html");
    }

    public void addressmanage() {
        render("addressManage.html");
    }

    @Clear(GET.class)
    @Before(POST.class)
    public void avator() {
        renderJson(UserCenterService.ME.setAvator(getCookie(RedisKit.COOKIE_ID), getPara("avator")));
    }

}
