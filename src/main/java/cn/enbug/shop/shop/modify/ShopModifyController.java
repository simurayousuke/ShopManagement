package cn.enbug.shop.shop.modify;

import cn.enbug.shop.captcha.ImageCaptchaValidator;
import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.shop.HasShopInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
@Before({POST.class, NoUrlPara.class, HasShopInterceptor.class})
public class ShopModifyController extends BaseController {

    private static final ShopModifyService SRV = ShopModifyService.ME;

    @Before(NameModifyShopValidator.class)
    public void name() {
        String token = getCookie(RedisKit.TOKEN);
        String name = getPara("name");
        String ip = getIp();
        renderJson(SRV.modifyName(token, name, ip));
    }

    public void description() {
        String token = getCookie(RedisKit.TOKEN);
        String description = getPara("description");
        String ip = getIp();
        renderJson(SRV.modifyDescription(token, description, ip));
    }

    public void transfer() {
        String token = getCookie(RedisKit.TOKEN);
        String username = getPara("username");
        String password = getPara("pwd");
        String ip = getIp();
        renderJson(SRV.transfer(token, username, password, ip));
    }

    @Clear(HasShopInterceptor.class)
    @Before(ImageCaptchaValidator.class)
    public void add() {
        String token = getCookie(RedisKit.TOKEN);
        String name = getPara("name");
        String description = getPara("description");
        String ip = getIp();
        String avator = getPara("avator", "shop/default.jpg");
        renderJson(SRV.addShop(token, name, description, ip, avator));
    }

}
