package cn.enbug.shop.shop.all;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;

/**
 * 店铺列表
 *
 * @author Forrest Yang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({NoUrlPara.class, GET.class})
public class ShopAllController extends BaseController {

    public void index() {
        setAttr("allShop", ShopService.ME.getAll(true));
        render("index.html");
    }

}
