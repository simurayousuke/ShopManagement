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

package cn.enbug.shop.order;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.OrderService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({POST.class, NeedLogInInterceptor.class})
public class OrderController extends BaseController {

    @Before(CreateOrderValidator.class)
    public void create() {
        String token = getCookie(RedisKit.COOKIE_ID);
        int addressId = getParaToInt("address");
        try {
            if (OrderService.ME.createOrderFromShopCar(token, addressId)) {
                renderJson(Ret.succeed());
            } else {
                renderJson(Ret.fail("Fail."));
            }
        } catch (RuntimeException e) {
            renderJson(Ret.fail(e.getMessage()));
        }
    }

    public void pay() {
        String token = getCookie(RedisKit.COOKIE_ID);
        String order = getPara("order");
        if (OrderService.ME.payOrder(token, order)) {
            renderJson(Ret.succeed());
        } else {
            renderJson(Ret.fail("支付失败！"));
        }
    }

    public void refund() {
        String token = getCookie(RedisKit.COOKIE_ID);
        String order = getPara("order");
        int id = getParaToInt("id");
        if (OrderService.ME.refundGood(token, order, id)) {
            renderJson(Ret.succeed());
        } else {
            renderJson(Ret.fail("支付失败！"));
        }
    }

}
