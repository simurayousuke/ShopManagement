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

package cn.ssdut153.shop.captcha;

import cn.ssdut153.shop.common.controller.BaseController;
import cn.ssdut153.shop.common.kit.Ret;
import cn.ssdut153.shop.common.service.ShortMessageCaptchaService;

/**
 * This is the captcha controller.
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class CaptchaController extends BaseController {

    public void image() {
        renderCaptcha();
    }

    public void phone() {
        String phone = getPara("phone");
        ShortMessageCaptchaService.me.generateCaptcha(phone);
        renderJson(Ret.succeed());
    }

}
