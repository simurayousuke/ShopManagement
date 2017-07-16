package cn.enbug.shop.common.render;

import cn.enbug.shop.captcha.ShopCaptcha;
import com.jfinal.captcha.Captcha;
import com.jfinal.captcha.CaptchaRender;
import com.jfinal.kit.StrKit;

/**
 * 自定义验证码Render
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class ShopCaptchaRender extends CaptchaRender {

    @Override
    protected Captcha createCaptcha() {
        String captchaKey = getCaptchaKeyFromCookie();
        if (StrKit.isBlank(captchaKey)) {
            captchaKey = StrKit.getRandomUUID();
        }
        return new ShopCaptcha(captchaKey, getRandomString(), Captcha.DEFAULT_EXPIRE_TIME);
    }

}
