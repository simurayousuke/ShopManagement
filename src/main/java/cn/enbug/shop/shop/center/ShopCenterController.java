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

package cn.enbug.shop.shop.center;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.Shop;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * 用户中心
 *
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({NoUrlPara.class, NeedLogInInterceptor.class})
public class ShopCenterController extends BaseController {

    private static final ShopService SHOP_SRV = ShopService.ME;

    public void index() {
        Shop shop = SHOP_SRV.findShopByToken(getCookie(RedisKit.TOKEN));
        setAttr("shop", shop);
        render("index.html");
    }

    public void newShop() {
        render("newShop.html");
    }

}
