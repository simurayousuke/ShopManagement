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
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class ShopService {

    public static final ShopService ME = new ShopService();
    private static final Shop SHOP_DAO = new Shop().dao();
    private static final UserService USER_SRV = UserService.ME;

    private ShopService() {

    }

    private boolean hasShop(User user) {
        Shop shop = findShopByUser(user);
        return null != shop;
    }

    private boolean isExist(String shopName) {
        return null != findShopByShopName(shopName);
    }

    /**
     * find shop by user.
     *
     * @param user User Object
     * @return Shop Object
     */
    public Shop findShopByUser(User user) {
        if (null == user) {
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
        if (null == token) {
            return null;
        }
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
        if (null == shopName) {
            return null;
        }
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
        if (null == user) {
            return false;
        }
        return Db.tx(4, () -> shop.save() &&
                new Log().setIp(ip).setOperation("createShop").setUserId(user.getId()).save());
    }

    /**
     * modify shop name.
     *
     * @param token owner token
     * @param name  shop name
     * @param ip    ip address
     * @return boolean
     */
    public boolean modifyName(String token, String name, String ip) {
        if (isExist(name)) {
            return false;
        }
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (null == shop) {
            return false;
        }
        shop.setShopName(name);
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("modifyShopName").setUserId(user.getId()).save());
    }

    /**
     * modify shop description.
     *
     * @param token       owner token
     * @param description shop description
     * @param ip          ip address
     * @return boolean
     */
    public boolean modifyDescription(String token, String description, String ip) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (null == shop) {
            return false;
        }
        shop.setDescription(description);
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("modifyShopDescription").setUserId(user.getId()).save());
    }

    /**
     * transfer shop to other user.
     *
     * @param token    owner token
     * @param username new owner username
     * @param ip       ip address
     * @return boolean
     */
    public boolean transfer(String token, String username, String ip) {
        User to = USER_SRV.findUserByUsername(username);
        if (null == to) {
            return false;
        }
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Shop shop = findShopByUser(user);
        if (null == shop) {
            return false;
        }
        shop.setOwnerUserId(to.getId());
        return Db.tx(4, () -> shop.update() &&
                new Log().setIp(ip).setOperation("transferShop").setUserId(user.getId()).save());
    }

}
