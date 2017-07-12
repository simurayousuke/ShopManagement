package cn.enbug.shop.search;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import com.jfinal.aop.Before;

/**
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.
 */
@Before(UserInterceptor.class)
public class SearchController extends BaseController {

    public void index() {
        render("index.html");
    }

}
