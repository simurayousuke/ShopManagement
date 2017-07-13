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

import cn.enbug.shop.common.builder.OpenSearchPushRequestBuilder;
import cn.enbug.shop.common.model.Good;
import cn.enbug.shop.common.model.Shop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Good service.
 *
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
public class GoodService {

    public static final GoodService ME = Duang.duang(GoodService.class);
    public static final OpenSearchService OPEN_SEARCH_SERVICE = OpenSearchService.ME;
    private static final Logger LOG = LoggerFactory.getLogger(GoodService.class);
    private static final Good GOOD_DAO = new Good().dao();

    /**
     * insert.
     *
     * @param token       shop owner token
     * @param ip          ip address
     * @param goodName    good name
     * @param description good description
     * @param price       good price
     * @param avator      good avator
     * @return boolean
     */
    @Before(Tx.class)
    public boolean insert(String token, String ip, String goodName, String description, double price, String avator, int number) throws IOException {
        // name, shop id,uuid, description, price, avator
        Shop shop = ShopService.ME.findShopByToken(token);
        if (null == shop) {
            return false;
        }
        int shopId = shop.getId();
        Good good = new Good().setGoodName(goodName).setShopId(shopId).setUuid(StrKit.getRandomUUID())
                .setDescription(description).setPrice(price).setAvator(avator);
        if (!good.save()) {
            return false;
        }
        //String id, String name, String description, int shopId, String avator, int saleCount, double price, int status
        OpenSearchPushRequestBuilder builder = new OpenSearchPushRequestBuilder(good.getId().toString(), goodName, description, shopId, avator, 0, price, 1, number);
        try {
            OPEN_SEARCH_SERVICE.add(builder.build());
        } catch (IOException e) {
            LOG.error(e.toString(), e);
            throw e;
        }
        return true;
    }

    /**
     * find good by id.
     *
     * @param id good id
     * @return Good Object
     */
    public Good findGoodById(int id) {
        return GOOD_DAO.findFirst(GOOD_DAO.getSqlPara("good.findById", id));
    }

    /**
     * find good by uuid.
     *
     * @param uuid good uuid
     * @return Good Object
     */
    public Good findGoodByUuid(String uuid) {
        if (null == uuid) {
            return null;
        }
        return GOOD_DAO.findFirst(GOOD_DAO.getSqlPara("good.findByUuid", uuid));
    }

    /**
     * delete.
     *
     * @param id good id
     */
    public void del(int id) {
        Good good = findGoodById(id);
        if (null != good) {
            good.setStatus(0);
        }
    }

    /**
     * delete.
     *
     * @param uuid good uuid
     */
    public void del(String uuid) {
        Good good = findGoodByUuid(uuid);
        if (null != good) {
            good.setStatus(0);
        }
    }

}
