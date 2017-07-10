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

package cn.ssdut153.shop.register;

import cn.ssdut153.shop.common.kit.Ret;
import cn.ssdut153.shop.common.service.UserService;

/**
 * 注册服务
 *
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
class RegisterService {

    static final RegisterService ME = new RegisterService();
    private static final UserService SRV = UserService.getInstance();

    /**
     * 邮箱注册
     *
     * @param email email
     * @param ip    ip地址
     * @return 结果
     */
    Ret registerByEmail(String email, String ip) {
        boolean b = SRV.initUserByEmail(email);
        return b ? Ret.succeed() : Ret.fail();
    }

    /**
     * 手机号注册
     *
     * @param phone 手机号
     * @param ip    ip地址
     * @return 结果
     */
    Ret registerByPhone(String phone, String ip) {
        boolean b = SRV.initUserByPhoneNumber(phone);
        return b ? Ret.succeed() : Ret.fail();
    }

}
