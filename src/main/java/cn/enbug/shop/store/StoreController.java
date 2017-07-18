package cn.enbug.shop.store;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.service.GoodService;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;

/**
 * @author Forrest Yang
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
@Before(GET.class)
public class StoreController extends BaseController {

    public void index() {
        setAttr("list", GoodService.ME.getGoodListByShopId(ShopService.ME.findShopByUuid(getPara()).getId()));
        render("index.html");
    }

}
