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
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.7
 * @since 1.0.0
 */
@Before({POST.class, NoUrlPara.class, NeedLogInInterceptor.class})
public class UserModifyController extends BaseController {

    private static final UserModifyService SRV = UserModifyService.ME;

    @Before(AvatorModifyValidator.class)
    public void avator() {
        renderJson(SRV.setAvator(getCookie(RedisKit.COOKIE_ID), getPara("avator", "user/default.jpg")));
    }

    @Before(AddAddressValidator.class)
    public void addaddress() {
        renderJson(SRV.addAddress(getCookie(RedisKit.COOKIE_ID), getPara("name"),
                getPara("phone"), getPara("address")));
    }

    @Before(ChargeValidator.class)
    public void charge() {
        renderJson(SRV.charge(getCookie(RedisKit.COOKIE_ID), getParaToBigDecimal("value")));
    }

    @Before(DefaultAddressValidator.class)
    public void defaultaddress() {
        renderJson(SRV.setDefaultAddress(getCookie(RedisKit.COOKIE_ID), getParaToInt("id")));
    }

    @Before(BindPhoneValidator.class)
    public void bindphone() {
        renderJson(SRV.bindPhone(getCookie(RedisKit.COOKIE_ID), getPara("phone")));
    }

    @Before(BindEmailValidator.class)
    public void bindemail() {
        renderJson(SRV.bindEmail(getCookie(RedisKit.COOKIE_ID), getPara("email")));
    }

    @Before(ChangePwdValidator.class)
    public void changepwd() {
        renderJson(SRV.modifyPwd(getCookie(RedisKit.COOKIE_ID), getPara("old_pwd"), getPara("new_pwd")));
    }

    @Clear({POST.class, NoUrlPara.class})
    @Before(GET.class)
    public void active() {
        SRV.activeEmail(getPara());
        redirect("/user/center");
    }

}
