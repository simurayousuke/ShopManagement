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
 *  limitations under the License.
 */

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Duang;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class AddressService {

    // todo 完成功能

    public static final AddressService ME= Duang.duang(AddressService.class);

    public boolean verifyAddress(User user, int addressId){
        return false;
    }
}
