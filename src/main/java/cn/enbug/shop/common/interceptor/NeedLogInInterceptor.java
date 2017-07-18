package cn.enbug.shop.common.interceptor;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.UserService;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class NeedLogInInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        User user = UserService.ME.validateToken(c.getCookie(RedisKit.COOKIE_ID));
        if (null == user) {
            String redirect = c.getRequest().getRequestURI();
            redirect = UrlKit.encode(redirect, "utf-8");
            c.redirect("/login?" + redirect);
        } else {
            if (null == user.getPhone() && !("/user/center/bindphone".equals(c.getRequest().getRequestURI()) ||
                    "/user/modify/bindphone".equals(c.getRequest().getRequestURI()))) {
                c.redirect("/user/center/bindphone");
            } else {
                c.setAttr("user", user);
                inv.invoke();
            }
        }
    }

}
