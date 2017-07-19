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

package cn.enbug.shop.shop;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class HasShopInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        User user = RedisKit.getUserByToken(c.getCookie(RedisKit.COOKIE_ID));
        if (null == user) {
            c.redirect("/login");
            return;
        }
        if (null == ShopService.ME.findShopByUser(user)) {
            c.redirect("/shop/center");
        } else {
            c.setAttr("user", user);
            inv.invoke();
        }
    }
}
