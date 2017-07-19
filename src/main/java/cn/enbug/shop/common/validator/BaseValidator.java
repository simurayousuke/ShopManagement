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

package cn.enbug.shop.common.validator;

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.FileService;
import cn.enbug.shop.common.service.ShortMessageCaptchaService;
import cn.enbug.shop.common.service.UserService;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

import java.math.BigDecimal;

/**
 * This is the base validator.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.9
 * @see com.jfinal.validate.Validator
 * @since 1.0.0
 */
public abstract class BaseValidator extends Validator {

    private static final String PHONE_PATTERN = "^1[3|4|5|7|8][0-9]{9}$";
    private static final String MONEY_PATTERN = "^(([1-9][0-9]*)|([0-9]))(.\\d{1,2})$";

    public BaseValidator() {
        super();
        // 短路验证
        shortCircuit = true;
    }

    protected void validateBigDecimal(String field, BigDecimal min, BigDecimal max,
                                      String errorKey, String errorMessage) {
        String value = controller.getPara(field);
        if (StrKit.isBlank(value)) {
            addError(errorKey, errorMessage);
            return;
        }
        try {
            BigDecimal temp = new BigDecimal(value.trim());
            if (temp.compareTo(min) < 0 || temp.compareTo(max) > 0) {
                addError(errorKey, errorMessage);
            }
        } catch (NumberFormatException e) {
            LogKit.logNothing(e);
            addError(errorKey, errorMessage);
        }
    }

    /**
     * 验证手机号格式
     *
     * @param field        手机号表单字段
     * @param errorKey     错误key
     * @param errorMessage 错误信息
     */
    protected void validatePhone(String field, String errorKey, String errorMessage) {
        validateRegex(field, PHONE_PATTERN, errorKey, errorMessage);
    }

    /**
     * 验证短信验证码
     *
     * @param field1       手机号表单
     * @param field2       验证码表单字段
     * @param errorKey     错误key
     * @param errorMessage 错误信息
     */
    protected void validatePhoneCaptcha(String field1, String field2, String errorKey, String errorMessage) {
        String phone = controller.getPara(field1);
        String captcha = controller.getPara(field2);
        if (!ShortMessageCaptchaService.ME.validate(phone, captcha)) {
            addError(errorKey, errorMessage);
        }
    }

    protected void validateMoney(String field, String errorKey, String errorMessage) {
        validateRegex(field, MONEY_PATTERN, errorKey, errorMessage);
    }

    protected void validateLogin() {
        User user = UserService.ME.validateToken(controller.getCookie(RedisKit.COOKIE_ID));
        if (null == user) {
            addError(Ret.MSG, "need login");
        }
    }

    protected void validateFile(String field, String errorKey, String errorMessage) {
        String fileName = controller.getPara(field);
        if (null != fileName) {
            if (!FileService.ME.isFileExist(fileName)) {
                addError(errorKey, errorMessage);
            }
        }
    }

}
