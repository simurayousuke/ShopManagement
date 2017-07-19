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
 *  limitations under the License.
 */

package cn.enbug.shop.shop.order;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.service.OrderService;
import cn.enbug.shop.shop.HasShopInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * @author Yang Zhizhuang
 * @author Forrest Yang
 * @author Hu Wenqiang
 * @version 1.0.5
 * @since 1.0.0
 */
@Before({GET.class, NoUrlPara.class, HasShopInterceptor.class})
public class ShopOrderController extends BaseController {

    public void index() {
        setAttr("normalOrders", OrderService.ME.getUnPayedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        setAttr("payedOrders", OrderService.ME.getUnSentListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        setAttr("doneOrders", OrderService.ME.getCheckedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        setAttr("refundOrders", OrderService.ME.getRefundingListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        setAttr("closedOrders", OrderService.ME.getClosedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        setAttr("sendingOrders", OrderService.ME.getSendingListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("all.html");
    }

    public void nopay() {
        setAttr("normalOrders", OrderService.ME.getUnPayedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("noPayPage.html");
    }

    public void nosend() {
        setAttr("payedOrders", OrderService.ME.getUnSentListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("noSendPage.html");
    }

    public void done() {
        setAttr("doneOrders", OrderService.ME.getCheckedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("donePage.html");
    }

    public void refund() {
        setAttr("refundOrders", OrderService.ME.getRefundingListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("refundPage.html");
    }

    public void close() {
        setAttr("closedOrders", OrderService.ME.getClosedListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("closePage.html");
    }

    public void norec() {
        setAttr("sendingOrders", OrderService.ME.getSendingListByTokenForSeller(getCookie(RedisKit.COOKIE_ID)));
        render("noRecPage.html");
    }

}
