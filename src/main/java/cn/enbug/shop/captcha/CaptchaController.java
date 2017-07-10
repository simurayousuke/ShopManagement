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

package cn.enbug.shop.captcha;

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.service.ShortMessageCaptchaService;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.ext.interceptor.POST;

/**
 * This is the captcha controller.
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before(NoUrlPara.class)
public class CaptchaController extends BaseController {

    /**
     * 图片验证码
     */
    @Before(GET.class)
    public void image() {
        renderCaptcha();
    }

    /**
     * 短信验证码
     */
    @Before({ImageCaptchaValidator.class, POST.class})
    public void phone() {
        String phone = getPara("phone");
        ShortMessageCaptchaService.ME.generateCaptchaAndSend(phone);
        renderJson(Ret.succeed());
    }

}
