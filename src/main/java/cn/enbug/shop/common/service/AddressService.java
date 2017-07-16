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

import cn.enbug.shop.common.exception.ModifyDefaultAddressException;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.Address;
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

/**
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
public class AddressService {

    public static final AddressService ME = Duang.duang(AddressService.class);
    private static final Address ADDRESS_DAO = new Address().dao();

    public Address getById(int id) {
        return ADDRESS_DAO.findFirst(ADDRESS_DAO.getSqlPara("address.findById", id));
    }

    public List getListByUserId(int userId) {
        return ADDRESS_DAO.find(ADDRESS_DAO.getSqlPara("address.findByUserId", userId));
    }

    public Address getDefaultByUserId(int userId) {
        return ADDRESS_DAO.findFirst(ADDRESS_DAO.getSqlPara("address.findDefaultByUserId", userId));
    }

    public boolean verifyAddress(User user, int addressId) {
        Address address = getById(addressId);
        return null != address && address.getUserId().equals(user.getId());
    }

    public Address verifyAddress(String token, int addressId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return null;
        }
        Address address = getById(addressId);
        if (null == address) {
            return null;
        }
        if (address.getUserId().equals(user.getId())) {
            return address;
        } else {
            return null;
        }
    }

    public boolean insert(String token, String name, String number, String address) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Address add = new Address();
        add.setUserId(user.getId());
        add.setReciverName(name);
        add.setPhoneNumber(number);
        add.setAddress(address);
        return add.save();
    }

    public boolean modify(String token, int addressId, String name, String number, String address) {
        Address add = verifyAddress(token, addressId);
        return null != add && add.setReciverName(name).setPhoneNumber(number).setAddress(address).update();
    }

    public boolean del(String token, int addressId) {
        Address address = verifyAddress(token, addressId);
        return null != address && address.delete();
    }

    @Before(Tx.class)
    public boolean setDefault(String token, int addressId) {
        User user = RedisKit.getUserByToken(token);
        if (null == user) {
            return false;
        }
        Address add = getById(addressId);
        if (null == add) {
            return false;
        }
        Address address = getDefaultByUserId(user.getId());
        if (null != address) {
            address.setIsDefault(0);
            if (!address.update()) {
                throw new ModifyDefaultAddressException("Fail to modify default address.(0)");
            }
        }
        add.setIsDefault(1);
        if (!add.update()) {
            throw new ModifyDefaultAddressException("Fail to modify default address.(1)");
        }
        return true;
    }

}
