/*
 * Copyright (c) 2017 EnBug Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enbug.shop.register;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.validator.BaseValidator;
import cn.enbug.shop.login.LoginService;
import com.jfinal.core.Controller;

/**
 * 邮箱注册验证器
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.3
 * @since 1.0.0
 */
public class EmailRegisterValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        validateEmail("email", Ret.MSG, "wrong format email");
        User user = LoginService.me.findUserByEmail(c.getPara("email"));
        if (null != user && user.getEmailStatus() != 0) {
            addError(Ret.MSG, "email already used");
        }
    }

}
