package cn.enbug.shop.shop.all;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.service.ShopService;

/**
 * 店铺列表
 *
 * @author Forrest Yang
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShopAllController extends BaseController {
    public void index() {
        setAttr("allShop", ShopService.ME.getAll(true));
        render("index.html");
    }
}
