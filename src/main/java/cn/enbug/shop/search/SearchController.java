package cn.enbug.shop.search;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.service.OpenSearchService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.StrKit;

import java.util.List;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.4
 * @since 1.0.
 */
@Before({GET.class, UserInterceptor.class})
public class SearchController extends BaseController {

    public void index() {
        String word = getPara();
        if (StrKit.notBlank(word)) {
            word = UrlKit.decode(word, "utf-8");
            List arrayList = OpenSearchService.ME.search("name", UrlKit.decode(getPara(), "utf-8"));
            if (null != arrayList) {
                setAttr("list", arrayList);
            }
            setAttr("word", word);
        }
        render("index.html");
    }

}
