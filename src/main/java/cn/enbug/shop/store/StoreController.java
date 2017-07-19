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
 * @author Hu Wenqiang
 * @version 1.0.3
 * @since 1.0.0
 */
@Before({GET.class, UserInterceptor.class})
public class StoreController extends BaseController {

    private static final ShopService SHOP_SRV = ShopService.ME;
    private static final GoodService GOOD_SRV = GoodService.ME;

    public void index() {
        String para = getPara();
        Shop shop = SHOP_SRV.findShopByUuid(para);
        if (null == shop) {
            shop = SHOP_SRV.findShopById(getParaToInt());
        }
        setAttr("shop", shop);
        setAttr("list", GOOD_SRV.getGoodListByShopId(shop.getId()));
        render("index.html");
    }

}
