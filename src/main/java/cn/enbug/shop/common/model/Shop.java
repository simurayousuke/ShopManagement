package cn.enbug.shop.common.model;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.base.BaseShop;
import com.jfinal.kit.StrKit;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class Shop extends BaseShop<Shop> {

    public Shop() {
        // empty constructor
    }

    /**
     * constructor
     *
     * @param name        shop name
     * @param description shop description
     * @param token       owner token
     * @param avator      avator id
     */
    public Shop(String name, String description, String token, int avator) {
        setShopName(name);
        setDescription(description);
        setUuid(StrKit.getRandomUUID());
        setOwnerUserId(RedisKit.getUserByToken(token).getId());
        setAvator(avator);
    }

    /**
     * constructor
     *
     * @param name        shop name
     * @param description shop description
     * @param token       owner token
     */
    public Shop(String name, String description, String token) {
        setShopName(name);
        setDescription(description);
        setUuid(StrKit.getRandomUUID());
        setOwnerUserId(RedisKit.getUserByToken(token).getId());
    }

    /**
     * constructor
     *
     * @param name        shop name
     * @param description shop description
     * @param userId      user id
     */
    public Shop(String name, String description, int userId) {
        setShopName(name);
        setDescription(description);
        setUuid(StrKit.getRandomUUID());
        setOwnerUserId(userId);
    }

    /**
     * constructor
     *
     * @param name        shop name
     * @param description shop description
     * @param userId      user id
     * @param avator      avator id
     */
    public Shop(String name, String description, int userId, int avator) {
        setShopName(name);
        setDescription(description);
        setUuid(StrKit.getRandomUUID());
        setOwnerUserId(userId);
        setAvator(avator);
    }

}
