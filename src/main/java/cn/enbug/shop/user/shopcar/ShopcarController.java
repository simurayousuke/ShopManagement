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

package cn.enbug.shop.user.shopcar;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.service.AddressService;
import cn.enbug.shop.common.service.ShopCarService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({NoUrlPara.class, NeedLogInInterceptor.class})
public class ShopcarController extends BaseController {

    @Before(GET.class)
    public void index() {
        String token = getCookie(RedisKit.COOKIE_ID);
        setAttr("shopcars", ShopCarService.ME.getShopCarRecordListByToken(token));
        setAttr("addresses", AddressService.ME.getListByToken(token));
        render("index.html");
    }

    @Before({POST.class, AddShopcarValidator.class})
    @Clear(NeedLogInInterceptor.class)
    public void add() {
        renderJson(ShopcarService.ME.add(getCookie(RedisKit.COOKIE_ID),
                getPara("uuid"), getParaToInt("count")));
    }

    @Before({POST.class, ModifyShopcarValidator.class})
    public void modify() {
        renderJson(ShopcarService.ME.modifyCount(getCookie(RedisKit.COOKIE_ID),
                getParaToInt("id"), getParaToInt("count")));
    }

    @Before({POST.class, DelShopCarValidator.class})
    public void del() {
        renderJson(ShopcarService.ME.del(getCookie(RedisKit.COOKIE_ID),
                getParaToInt("id")));
    }

}
