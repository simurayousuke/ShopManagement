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

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.model.Good;
import cn.enbug.shop.common.model.Shop;
import com.jfinal.aop.Duang;
import com.jfinal.kit.StrKit;

/**
 * Good service.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class GoodService {

    public static final GoodService ME = Duang.duang(GoodService.class);
    private static final Good GOOD_DAO = new Good().dao();

    private GoodService() {
        // singleton
    }

    public boolean insert(String token, String ip, String goodName, String description, double price, String avator) {
        // name, shop id,uuid, description, price, avator
        Shop shop = ShopService.ME.findShopByToken(token);
        if (null == shop) {
            return false;
        }
        int shopId = shop.getId();
        Good good = new Good().setGoodName(goodName).setShopId(shopId).setUuid(StrKit.getRandomUUID())
                .setDescription(description).setPrice(price).setAvator(avator);
        return good.save();
    }

    public void del(int id) {

    }

    public void del(String uuid) {

    }

}
