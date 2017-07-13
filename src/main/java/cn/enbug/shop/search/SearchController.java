package cn.enbug.shop.search;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.interceptor.UserInterceptor;
import cn.enbug.shop.common.kit.UrlKit;
import cn.enbug.shop.common.service.OpenSearchService;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.
 */
@Before(UserInterceptor.class)
public class SearchController extends BaseController {

    public void index() {
        render("index.html");
    }

    public void s() throws IOException {
        StringBuilder sb = new StringBuilder();
        ArrayList arrayList = OpenSearchService.ME.search("name", UrlKit.decode(getPara(),"utf-8"));
        if (arrayList == null) {
            renderText("none");
            return;
        }
       /* for (Object o : arrayList) {
            HashMap map = JsonKit.parse(o.toString(), HashMap.class);
            sb.append(map.get("name")+"\t");
            sb.append(map.get("description")+"\t");
            sb.append(map.get("price")+"\n");
        }*/
       setAttr("list",arrayList);
        render("result.html");
    }

}
