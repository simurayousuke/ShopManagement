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
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Good service.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.5
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class GoodService {

    public static final GoodService ME = Duang.duang(GoodService.class);
    private static final OpenSearchService OPEN_SEARCH_SERVICE = OpenSearchService.ME;
    private static final Logger LOG = LoggerFactory.getLogger(GoodService.class);
    private static final Good GOOD_DAO = new Good().dao();

    public List<Good> getGoodListByShopId(int id) {
        return GOOD_DAO.find(GOOD_DAO.getSqlPara("good.findByShopId", id));
    }

    public List<Good> getGoodListByToken(String token) {
        Shop shop = ShopService.ME.findShopByToken(token);
        return null == shop ? new ArrayList<>() : getGoodListByShopId(shop.getId());
    }

    /**
     * insert.
     *
     * @param token       shop owner token
     * @param ip          ip address
     * @param goodName    good name
     * @param description good description
     * @param price       good price
     * @param avator      good avator
     * @param number      number
     * @return boolean
     */
    @Before(Tx.class)
    public boolean insert(String token, String ip, String goodName, String description, BigDecimal price, String avator, int number) {
        // name, shop id,uuid, description, price, avator
        Shop shop = ShopService.ME.findShopByToken(token);
        if (null == shop) {
            return false;
        }
        int shopId = shop.getId();
        String shopName = shop.getShopName();
        int ownerId = shop.getOwnerUserId();
        String ownerName = UserService.ME.findUserById(ownerId).getUsername();
        String uuid = StrKit.getRandomUUID();
        Good good = new Good().setGoodName(goodName).setShopId(shopId).setUuid(uuid)
                .setDescription(description).setPrice(price).setAvator(avator).setNumber(number);
        if (!good.save()) {
            return false;
        }
        //String id, String name, String description, int shopId, String avator, int saleCount, BigDecimal price, int status
        Kv kv = Kv.by("id", good.getId().toString())
                .set("name", goodName)
                .set("description", description)
                .set("shop_id", shopId)
                .set("avator", avator)
                .set("sale_count", 0)
                .set("price", price)
                .set("status", 1)
                .set("number", number)
                .set("uuid", uuid)
                .set("shop_name", shopName)
                .set("owner_id", ownerId)
                .set("owner_name", ownerName);
        OPEN_SEARCH_SERVICE.add(kv);
        return true;
    }

    /**
     * update good
     *
     * @param token       token
     * @param uuid        good uuid
     * @param goodName    good name
     * @param description description
     * @param price       price
     * @param avator      avator
     * @param number      number
     * @return boolean
     */
    @Before(Tx.class)
    public boolean update(String token, String uuid, String goodName, String description, BigDecimal price, String avator, int number) {
        Shop shop = ShopService.ME.findShopByToken(token);
        Good good = findGoodByUuid(uuid);
        if (null == shop || null == good) {
            return false;
        }
        good.setGoodName(goodName).setDescription(description)
                .setPrice(price).setAvator(avator).setNumber(number);
        if (shop.getId() != good.getShopId()) {
            return false;
        }
        if (!good.update()) {
            return false;
        }
        Kv kv = Kv.by("name", goodName)
                .set("description", description)
                .set("avator", avator)
                .set("price", price)
                .set("number", number);
        OPEN_SEARCH_SERVICE.update(kv);
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
        return null == uuid ? null : GOOD_DAO.findFirst(GOOD_DAO.getSqlPara("good.findByUuid", uuid));
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
