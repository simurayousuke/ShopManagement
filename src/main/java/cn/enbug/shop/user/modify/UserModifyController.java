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

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.5
 * @since 1.0.0
 */
@Before({POST.class, NeedLogInInterceptor.class})
public class UserModifyController extends BaseController {

    @Before(AvatorModifyValidator.class)
    public void avator() {
        renderJson(UserModifyService.ME.setAvator(getCookie(RedisKit.COOKIE_ID), getPara("avator")));
    }

    @Before(AddAddressValidator.class)
    public void addaddress() {
        renderJson(UserModifyService.ME.addAddress(getCookie(RedisKit.COOKIE_ID), getPara("name"),
                getPara("phone"), getPara("address")));
    }

    @Before(ChargeValidator.class)
    public void charge() {
        renderJson(UserModifyService.ME.charge(getCookie(RedisKit.COOKIE_ID), getParaToBigDecimal("value")));
    }

    @Before(DefaultAddressValidator.class)
    public void defaultaddress() {
        renderJson(UserModifyService.ME.setDefaultAddress(getCookie(RedisKit.COOKIE_ID), getParaToInt("id")));
    }

    @Before(BindPhoneValidator.class)
    public void bindphone() {
        renderJson(UserModifyService.ME.bindPhone(getCookie(RedisKit.COOKIE_ID), getPara("phone")));
    }

    @Before(BindEmailValidator.class)
    public void bindemail() {
        renderJson(UserModifyService.ME.bindEmail(getCookie(RedisKit.COOKIE_ID), getPara("email")));
    }

    @Clear(POST.class)
    public void active() {
        UserModifyService.ME.activeEmail(getPara());
        redirect("/user/center");

    }

}
