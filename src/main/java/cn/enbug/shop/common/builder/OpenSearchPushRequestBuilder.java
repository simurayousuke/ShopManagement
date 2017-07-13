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

package cn.enbug.shop.common.builder;

import com.jfinal.kit.JsonKit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenSearchPushRequestBuilder {

    private ArrayList<HashMap<String, Object>> list;

    /**
     * constructor
     */
    public OpenSearchPushRequestBuilder() {
        list = new ArrayList<>();
    }

    /**
     * constructor
     *
     * @param id          id
     * @param name        good name
     * @param description good description
     * @param shopId      shop id
     * @param avator      avator url
     * @param saleCount   sale count
     * @param price       price
     * @param status      status
     */
    public OpenSearchPushRequestBuilder(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status) {
        list = new ArrayList<>();

    }

    /**
     * add
     *
     * @param id          id
     * @param name        good name
     * @param description good description
     * @param shopId      shop id
     * @param avator      avator url
     * @param saleCount   sale count
     * @param price       price
     * @param status      status
     */
    public void add(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("name", name);
        fields.put("description", description);
        fields.put("shop_id", shopId);
        fields.put("avator", avator);
        fields.put("sale_count", saleCount);
        fields.put("price", price);
        fields.put("status", status);
        HashMap<String, Object> unit = new HashMap<>();
        unit.put("fields", fields);
        unit.put("cmd", "ADD");
        list.add(unit);
    }

    /**
     * update
     *
     * @param id          id
     * @param name        good name
     * @param description good description
     * @param shopId      shop id
     * @param avator      avator url
     * @param saleCount   sale count
     * @param price       price
     * @param status      status
     */
    public void update(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("name", name);
        fields.put("description", description);
        fields.put("shop_id", shopId);
        fields.put("avator", avator);
        fields.put("sale_count", saleCount);
        fields.put("price", price);
        fields.put("status", status);
        HashMap<String, Object> unit = new HashMap<>();
        unit.put("fields", fields);
        unit.put("cmd", "UPDATE");
        list.add(unit);
    }

    /**
     * delete
     *
     * @param id          id
     * @param name        good name
     * @param description good description
     * @param shopId      shop id
     * @param avator      avator url
     * @param saleCount   sale count
     * @param price       price
     * @param status      status
     */
    public void del(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("name", name);
        fields.put("description", description);
        fields.put("shop_id", shopId);
        fields.put("avator", avator);
        fields.put("sale_count", saleCount);
        fields.put("price", price);
        fields.put("status", status);
        HashMap<String, Object> unit = new HashMap<>();
        unit.put("fields", fields);
        unit.put("cmd", "DELETE");
        list.add(unit);
    }

    public String build() {
        return JsonKit.toJson(list);
    }

}
