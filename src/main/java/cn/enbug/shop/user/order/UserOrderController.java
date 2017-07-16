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

import java.util.List;

/**
 * @author Yang Zhizhuang
 * @author Forrest Yang
 * @author Hu Wenqiang
 * @version 1.0.5
 * @since 1.0.0
 */
public class UserOrderController extends BaseController {

    public void index() {
        List<OrderNumber> orderNumbers = OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID));
        if (null != orderNumbers) {
            setAttr("normalOrders", UserOrderService.ME.getUnpayedList(orderNumbers));
        }
        setAttr("normalOrders", UserOrderService.ME.getUnpayedList(OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID))));
        setAttr("payedOrders", OrderService.ME.getUnSentListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("sendingOrders", OrderService.ME.getSendingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("nocmtOrders", OrderService.ME.getCheckedNotCommentedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("doneOrders", OrderService.ME.getCheckedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("refundOrders", OrderService.ME.getRefundingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("closedOrders", OrderService.ME.getClosedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("all.html");
    }

    public void nopay() {
        setAttr("normalOrders", UserOrderService.ME.getUnpayedList(OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID))));
        render("noPayPage.html");
    }

    public void nosend() {
        setAttr("payedOrders", OrderService.ME.getUnSentListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noSendPage.html");
    }

    public void norec() {
        setAttr("sendingOrders", OrderService.ME.getSendingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noRecPage.html");
    }

    public void nocmt() {
        setAttr("nocmtOrders", OrderService.ME.getCheckedNotCommentedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noCmtPage.html");
    }

    public void done() {
        setAttr("doneOrders", OrderService.ME.getCheckedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("donePage.html");
    }

    public void refund() {
        setAttr("refundOrders", OrderService.ME.getRefundingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("refundPage.html");
    }

    public void close() {
        setAttr("closedOrders", OrderService.ME.getClosedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("closePage.html");
    }
}
