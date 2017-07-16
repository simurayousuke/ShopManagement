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

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.ShopCarService;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShopcarService {

    public static final ShopcarService ME = new ShopcarService();
    private static final ShopCarService SRV = ShopCarService.ME;

    public Ret add(String token, String uuid, int count) {
        return SRV.add(token, uuid, count) ? Ret.succeed() : Ret.fail("Fail.");
    }

    public Ret modifyCount(String token, String uuid, int count) {
        return SRV.modifyCount(token, uuid, count) ? Ret.succeed() : Ret.fail("Fail.");
    }

    public Ret del(String token, String uuid) {
        return SRV.del(token, uuid) ? Ret.succeed() : Ret.fail("Fail.");
    }

}
