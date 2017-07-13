package cn.enbug.shop.shop.modify;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.shop.HasShopInterceptor;
import com.jfinal.aop.Before;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
@Before(HasShopInterceptor.class)
public class ShopModifyController extends BaseController {

    private static final ShopModifyService SRV = ShopModifyService.ME;

    public void name() {
        String token = getCookie(RedisKit.TOKEN);
        String name = getPara("name");
        String ip = getIp();
        renderJson(SRV.modifyName(token, name, ip));
    }

    public void description() {
        String token = getCookie(RedisKit.TOKEN);
        String description = getPara("description");
        String ip = getIp();
        renderJson(SRV.modifyDescription(token, description, ip));
    }

    public void transfer() {
        String token = getCookie(RedisKit.TOKEN);
        String username = getPara("username");
        String password = getPara("pwd");
        String ip = getIp();
        renderJson(SRV.transfer(token, username, password, ip));
    }

}
