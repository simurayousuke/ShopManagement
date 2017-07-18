package cn.enbug.shop.store;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.model.Shop;
import cn.enbug.shop.common.service.GoodService;
import cn.enbug.shop.common.service.ShopService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;

/**
 * @author Forrest Yang
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before({GET.class, UserInterceptor.class})
public class StoreController extends BaseController {

    public void index() {
        String para = getPara();
        Shop shop = ShopService.ME.findShopByUuid(para);
        if (null == shop) {
            shop = ShopService.ME.findShopById(Integer.parseInt(para));
        }
        setAttr("shop", shop);
        setAttr("list", GoodService.ME.getGoodListByShopId(shop.getId()));
        render("index.html");
    }

}
