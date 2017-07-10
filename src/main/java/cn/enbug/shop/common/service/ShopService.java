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

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.model.Shop;
import cn.enbug.shop.common.model.User;
import com.jfinal.plugin.activerecord.Db;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShopService {

    private static final Shop SHOP_DAO = new Shop().dao();
    /**
     * singleton
     */
    private static ShopService instance = new ShopService();

    private ShopService() {
    }

    /**
     * get instance.
     *
     * @return singleton
     */
    public static ShopService getInstance() {
        return instance;
    }

    private boolean hasShop(User user) {
        Shop shop = findShopByUser(user);
        return shop != null;
    }

    private boolean isExist(String shopName) {
        return findShopByShopName(shopName) != null;
    }

    /**
     * find shop by user.
     *
     * @param user User Object
     * @return Shop Object
     */
    public Shop findShopByUser(User user) {
        if (user == null) {
            return null;
        }
        return SHOP_DAO.findFirst(SHOP_DAO.getSqlPara("shop.findByOwnerUserId", user.getId()));
    }

    /**
     * find shop by user token.
     *
     * @param token owner token
     * @return Shop Object
     */
    public Shop findShopByToken(String token) {
        User user = RedisKit.getUserByToken(token);
        return findShopByUser(user);
    }

    /**
     * find shop by shop name.
     *
     * @param shopName shop name
     * @return Shop Object
     */
    public Shop findShopByShopName(String shopName) {
        return SHOP_DAO.findFirst(SHOP_DAO.getSqlPara("shop.findByShopName", shopName));
    }

    /**
     * create shop.
     *
     * @param name        shop name
     * @param description shop description
     * @param token       owner token
     * @param ip          ip address
     * @return boolean
     */
    public boolean createShop(String name, String description, String token, String ip) {
        Shop shop = new Shop(name, description, token);
        User user = RedisKit.getUserByToken(token);
        if (user == null) {
            return false;
        }
        return Db.tx(4, () -> shop.save() &&
                new Log().setIp(ip).setOperation("createShop").setUserId(user.getId()).save());
    }

    /**
     * modify shop name.
     *
     * @param token owner token
     * @param name shop name
     * @param ip ip address
     * @return boolean
     */
    public boolean modifyName(String token, String name, String ip) {
        if (isExist(name)) {
            return false;
        }
        User user = RedisKit.getUserByToken(token);
        if (user == null) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (shop == null) {
            return false;
        }
        shop.setShopName(name);
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("modifyShopName").setUserId(user.getId()).save());
    }

    /**
     * modify shop description.
     *
     * @param token owner token
     * @param description shop description
     * @param ip ip address
     * @return boolean
     */
    public boolean modifyDescription(String token, String description, String ip) {
        User user = RedisKit.getUserByToken(token);
        if (user == null) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (shop == null) {
            return false;
        }
        shop.setDescription(description);
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("modifyShopDescription").setUserId(user.getId()).save());
    }

    /**
     * transfer shop to other user.
     *
     * @param token owner token
     * @param username new owner username
     * @param ip ip address
     * @return boolean
     */
    public boolean transfer(String token, String username, String ip) {
        User to = UserService.getInstance().findUserByUsername(username);
        if (to == null) {
            return false;
        }
        User user = RedisKit.getUserByToken(token);
        if (user == null) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (shop == null) {
            return false;
        }
        shop.setOwnerUserId(to.getId());
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("transferShop").setUserId(user.getId()).save());
    }

}
