package cn.enbug.shop.shop.all;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * 店铺列表
 *
 * @author Forrest Yang
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({NoUrlPara.class, GET.class, UserInterceptor.class})
public class ShopAllController extends BaseController {

    public void index() {
        setAttr("allShop", ShopService.ME.getAll(true));
        render("index.html");
    }

}
