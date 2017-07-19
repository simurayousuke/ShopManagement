package cn.enbug.shop.register;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.validator.BaseValidator;
import cn.enbug.shop.login.LoginService;
import com.jfinal.core.Controller;

/**
 * 注册第二步数据验证
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class HandleStep2RegisterValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        validateRequired("activeCode", Ret.MSG, "请输入激活码");
        validateString("username", 1, 20, Ret.MSG, "用户名长度错误");
        validateString("pwd", 6, 32, Ret.MSG, "密码长度错误");
        String username = c.getPara("username");
        if (null != LoginService.me.findUserByUsername(username)) {
            addError(Ret.MSG, "User already exists.");
        }
    }

}
