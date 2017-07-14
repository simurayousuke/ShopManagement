package cn.enbug.shop.suggest;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.service.OpenSearchService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

import java.io.IOException;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class SuggestController extends BaseController {

    @Clear(NoUrlPara.class)
    @Before(POST.class)
    public void index() throws IOException {
        String key = getPara();
        if (null == key) {
            renderJson(Ret.fail("Fail"));
            return;
        }
        key = UrlKit.decode(key, "utf-8");
        String result = OpenSearchService.ME.suggest("default", key);
        if (null == result) {
            renderJson(Ret.fail("Fail"));
        } else {
            renderJson(Ret.succeed().set("suggestions", result));
        }
    }

}
