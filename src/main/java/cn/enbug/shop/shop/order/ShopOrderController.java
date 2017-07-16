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

import cn.enbug.shop.shop.HasShopInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;

/**
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({GET.class, HasShopInterceptor.class})
public class ShopOrderController {
    // todo 完成功能
}
