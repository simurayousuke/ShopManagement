package cn.enbug.shop.shop;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class HasShopInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        User user = RedisKit.getUserByToken(c.getCookie(RedisKit.COOKIE_ID));
        if (user == null) {
            c.redirect("/login");
            return;
        }
        if (ShopService.ME.findShopByUser(user) == null) {
            c.redirect("/shop/center");
        } else {
            c.setAttr("user", user);
            inv.invoke();
        }
    }
}
