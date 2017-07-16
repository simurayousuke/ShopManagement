package cn.enbug.shop.user.center;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;

/**
 * @author Forrest Yang
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({GET.class, NeedLogInInterceptor.class})
public class UserCenterController extends BaseController {

    public void index() {
        render("secure.html");
    }

    public void userinfo() {
        render("userinfo.html");
    }

    public void recharge() {
        render("recharge.html");
    }

}
