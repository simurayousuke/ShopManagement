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
import cn.enbug.shop.common.service.EmailService;
import cn.enbug.shop.common.service.ShortMessageCaptchaService;
import cn.enbug.shop.common.validator.BaseValidator;
import com.jfinal.core.Controller;

/**
 * 注册第二步检测
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class Step2RegisterValidator extends BaseValidator {

    @Override
    protected void validate(Controller c) {
        String code = c.getPara();
        if (!ShortMessageCaptchaService.ME.validateActiveCodeForPhoneNumber(code)
                && !EmailService.ME.validateActiveCodeForEmail(code)) {
            addError(Ret.MSG, "wrong code");
        }
    }

    @Override
    protected void handleError(Controller c) {
        c.redirect("/register");
    }

}
