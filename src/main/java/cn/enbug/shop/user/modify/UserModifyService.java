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

import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.AddressService;
import cn.enbug.shop.common.service.UserService;

import java.math.BigDecimal;

/**
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
public class UserModifyService {

    public static final UserModifyService ME = new UserModifyService();

    Ret setAvator(String token, String avator) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return Ret.fail("登录超时");
        }
        user.setAvator(avator);
        return user.update() ? Ret.succeed() : Ret.fail("设置失败");
    }

    Ret addAddress(String token, String name, String phone, String address) {
        return AddressService.ME.insert(token, name, phone, address) ? Ret.succeed() : Ret.fail("设置失败");
    }

    Ret charge(String token, BigDecimal value) {
        if (UserService.ME.charge(token, value)) {
            return Ret.succeed();
        } else {
            return Ret.fail("充值失败！");
        }
    }

}
