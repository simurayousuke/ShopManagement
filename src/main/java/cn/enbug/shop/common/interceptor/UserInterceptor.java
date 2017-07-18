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

package cn.enbug.shop.common.interceptor;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.UserService;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 导航栏用户数据
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        User user = UserService.ME.validateToken(c.getCookie(RedisKit.COOKIE_ID));
        c.setAttr("user", user);
        String key = inv.getControllerKey();
        String method = inv.getMethodName();
        if (method.equalsIgnoreCase("/index")) {
            method = "";
        }
        String redirect = key + method;
        redirect = UrlKit.encode(redirect, "utf-8");
        c.setAttr("redirect", redirect);
        inv.invoke();
    }

}
