package cn.enbug.shop.suggest;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class SuggestValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {

        String key = c.getPara();
        if (null == key) {
            addError(Ret.MSG, "Fail");
        }

    }

    @Override
    protected void handleError(Controller c) {
        c.setAttr(Ret.STATUS, true);
        c.renderJson();
    }

}
