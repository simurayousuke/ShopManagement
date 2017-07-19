package cn.enbug.shop.user.pay;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.NeedLogInInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * @author Forrest Yang
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({GET.class, NoUrlPara.class, NeedLogInInterceptor.class})
public class UserPayController extends BaseController {

    public void index() {
        render("index.html");
    }

}
