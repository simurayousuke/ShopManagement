package cn.enbug.shop.search;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.service.OpenSearchService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.kit.StrKit;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Yang Zhizhuang
 * @version 1.0.3
 * @since 1.0.
 */
@Before(UserInterceptor.class)
public class SearchController extends BaseController {

    @Clear(NoUrlPara.class)
    public void index() throws IOException {
        String word = getPara();
        if (!StrKit.isBlank(word)) {
            word = UrlKit.decode(word, "utf-8");
            ArrayList arrayList = OpenSearchService.ME.search("name", UrlKit.decode(getPara(), "utf-8"));
            if (arrayList != null) {
                setAttr("list", arrayList);
            }
            setAttr("word", word);
        }
        render("index.html");
    }

}
