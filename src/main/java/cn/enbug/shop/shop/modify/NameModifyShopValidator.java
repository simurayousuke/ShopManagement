package cn.enbug.shop.shop.modify;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.ShopService;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

/**
 * 修改店铺名称的验证器
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class NameModifyShopValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        if (ShopService.ME.isExist(c.getPara("name"))) {
            addError(Ret.MSG, "name exists");
        }
    }

    @Override
    protected void handleError(Controller c) {
        c.setAttr(Ret.STATUS, false);
        c.renderJson();
    }

}
