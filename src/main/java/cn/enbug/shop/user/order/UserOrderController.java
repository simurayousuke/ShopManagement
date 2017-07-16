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

package cn.enbug.shop.user.order;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.OrderNumber;
import cn.enbug.shop.common.service.OrderService;

import java.util.ArrayList;

/**
 * @author Yang Zhizhuang
 * @author Forrest Yang
 * @version 1.0.3
 * @since 1.0.0
 */
public class UserOrderController extends BaseController {
    public void index() {
        ArrayList<OrderNumber> orderNumbers = (ArrayList) OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID));
        if (null != orderNumbers) {
            setAttr("normalOrders", UserOrderService.ME.getUnpayedList(orderNumbers));
        }
        render("all.html");
    }

    // todo 完成功能
    @SuppressWarnings("unchecked")
    public void nopay() {
        ArrayList<OrderNumber> orderNumbers = (ArrayList) OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID));
        if (null != orderNumbers) {
            setAttr("normalOrders", UserOrderService.ME.getUnpayedList(orderNumbers));
        }
        render("noPayPage.html");
    }

    public void nosend() {
        render("noSendPage.html");
    }

    public void norec() {
        render("noRecPage.html");
    }

    public void nocmt() {
        render("noCmtPage.html");
    }

    public void done() {
        render("donePage.html");
    }

    public void refund() {
        render("refundPage.html");
    }

    public void close() {
        render("closePage.html");
    }
}
