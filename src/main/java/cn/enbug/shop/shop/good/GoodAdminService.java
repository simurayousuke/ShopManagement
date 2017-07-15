package cn.enbug.shop.shop.good;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.GoodService;

import java.math.BigDecimal;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class GoodAdminService {

    public static final GoodAdminService ME = new GoodAdminService();
    private static final GoodService GOOD_SERVICE = GoodService.ME;

    Ret add(String token, String ip, String name, String description, BigDecimal price, String avator, int number) {
        if (GOOD_SERVICE.insert(token, ip, name, description, price, avator, number)) {
            return Ret.succeed();
        } else {
            return Ret.fail("Fail");
        }
    }

}
