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

package cn.enbug.shop.user.modify;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.UserService;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

/**
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class BindPhoneValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        validatePhone("phone", Ret.MSG, "手机号格式错误");
        String phone = c.getPara("phone");
        User user = UserService.ME.findUserByPhoneNumber(phone);
        if (null != user) {
            addError(Ret.MSG, "手机号已被使用");
        }
        validatePhoneCaptcha("phone", "phone_captcha", Ret.MSG, "验证码错误");
    }

}
