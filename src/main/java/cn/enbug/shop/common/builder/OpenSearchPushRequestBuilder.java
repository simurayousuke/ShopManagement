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

import java.util.HashMap;

/**
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
public class OpenSearchPushRequestBuilder {

    private HashMap<String, Object> fields;

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
    public OpenSearchPushRequestBuilder(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status, int number,String uuid) {
        fields = new HashMap<>();
        fields.put("id", id);
        fields.put("name", name);
        fields.put("description", description);
        fields.put("shop_id", shopId);
        fields.put("avator", avator);
        fields.put("sale_count", saleCount);
        fields.put("price", price);
        fields.put("status", status);
        fields.put("number", number);
        fields.put("uuid", uuid);
    }

    public HashMap<String, Object> build() {
        return fields;
    }

}
