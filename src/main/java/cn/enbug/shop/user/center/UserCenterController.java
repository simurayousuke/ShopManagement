package cn.enbug.shop.user.center;

import cn.enbug.shop.common.controller.BaseController;

/**
 * Created by forre on 2017/7/16.
 */
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
