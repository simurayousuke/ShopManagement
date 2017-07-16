package cn.enbug.shop.good;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

/**
 * 商品uuid不存在拦截
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class GoodValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        String uuid = c.getPara();
        if (StrKit.isBlank(uuid)) {
            addError(Ret.MSG, "no uuid");
        }
    }

    @Override
    protected void handleError(Controller c) {
        c.redirect("/search");
    }

}
