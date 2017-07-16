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
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.OrderNumber;
import cn.enbug.shop.common.service.OrderService;
import cn.enbug.shop.common.service.UserService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;

import java.util.List;

/**
 * @author Yang Zhizhuang
 * @author Forrest Yang
 * @author Hu Wenqiang
 * @version 1.0.5
 * @since 1.0.0
 */
@Before({GET.class, NeedLogInInterceptor.class})
public class UserOrderController extends BaseController {

    private static final UserOrderService SRV = UserOrderService.ME;
    private static final UserService USER_SRV = UserService.ME;
    private static final OrderService ORDER_SRV = OrderService.ME;

    public void index() {
        List<OrderNumber> orderNumbers = ORDER_SRV.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID));
        if (null != orderNumbers) {
            setAttr("normalOrders", SRV.getUnpayedList(orderNumbers));
        }
        setAttr("normalOrders", SRV.getUnpayedList(OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID))));
        setAttr("payedOrders", ORDER_SRV.getUnSentListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("sendingOrders", ORDER_SRV.getSendingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("nocmtOrders", ORDER_SRV.getCheckedNotCommentedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("doneOrders", ORDER_SRV.getCheckedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("refundOrders", ORDER_SRV.getRefundingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        setAttr("closedOrders", ORDER_SRV.getClosedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("all.html");
    }

    public void nopay() {
        setAttr("normalOrders", SRV.getUnpayedList(OrderService.ME.getUnpayedOrderNumberList(getCookie(RedisKit.COOKIE_ID))));
        render("noPayPage.html");
    }

    public void nosend() {
        setAttr("payedOrders", ORDER_SRV.getUnSentListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noSendPage.html");
    }

    public void norec() {
        setAttr("sendingOrders", ORDER_SRV.getSendingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noRecPage.html");
    }

    public void nocmt() {
        setAttr("nocmtOrders", ORDER_SRV.getCheckedNotCommentedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("noCmtPage.html");
    }

    public void done() {
        setAttr("doneOrders", ORDER_SRV.getCheckedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("donePage.html");
    }

    public void refund() {
        setAttr("refundOrders", ORDER_SRV.getRefundingListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("refundPage.html");
    }

    public void close() {
        setAttr("closedOrders", ORDER_SRV.getClosedListByTokenForBuyer(getCookie(RedisKit.COOKIE_ID)));
        render("closePage.html");
    }

}
