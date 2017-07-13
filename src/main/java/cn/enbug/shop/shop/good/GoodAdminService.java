package cn.enbug.shop.shop.good;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.GoodService;
import com.jfinal.kit.LogKit;

import java.io.IOException;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class GoodAdminService {

    public static final GoodAdminService ME = new GoodAdminService();
    private static final GoodService GOOD_SERVICE = GoodService.ME;

    Ret add(String token, String ip, String name, String description, double price, String avator, int number) {
        try {
            if (GOOD_SERVICE.insert(token, ip, name, description, price, avator, number)) {
                return Ret.succeed();
            } else {
                return Ret.fail("Fail");
            }
        } catch (IOException e) {
            LogKit.logNothing(e);
            return Ret.fail("Error");
        }
    }

}
