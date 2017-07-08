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
import cn.ssdut153.shop.common.model.User;

/**
 * 注册服务
 *
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class RegisterService {

    public static final RegisterService me = new RegisterService();

    /**
     * 邮箱注册
     *
     * @param user  用户
     * @param email email
     * @param ip    ip地址
     * @return 结果
     */
    public Ret registerByEmail(User user, String email, String ip) {
        return Ret.fail();
    }

    /**
     * 手机号注册
     *
     * @param user 用户
     * @param ip   ip地址
     * @return 结果
     */
    public Ret registerByPhone(User user, String ip) {
        return Ret.fail();
    }

}
