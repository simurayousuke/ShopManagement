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

    public OpenSearchPushRequestBuilder() {
        list = new ArrayList<>();
    }

    public OpenSearchPushRequestBuilder(String id, String name, String description, int shopId, String avator, int saleCount, double price, int status) {
        list = new ArrayList<>();

    }

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

    public String build(){
        return JsonKit.toJson(list);
    }

}
