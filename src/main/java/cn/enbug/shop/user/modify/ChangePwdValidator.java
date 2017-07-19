package cn.enbug.shop.user.modify;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

public class ChangePwdValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        validateRequired("old_pwd", Ret.MSG, "请输入旧密码");
        validateString("new_pwd", 6, 32, Ret.MSG, "新密码长度错误");
    }

}
