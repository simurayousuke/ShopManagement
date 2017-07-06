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

package cn.ssdut153.shop.common.kit;

import com.jfinal.kit.JsonKit;

import java.util.HashMap;
import java.util.Map;

/**
 * The encapsulation of return.
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class Ret extends HashMap {

    public static final String STATUS = "status";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    private Ret() {
    }

    /**
     * create a ret with a k,v pair.
     *
     * @param key   key
     * @param value value
     * @return Ret
     */
    public static Ret by(Object key, Object value) {
        return new Ret().set(key, value);
    }

    /**
     * create an empty ret.
     *
     * @return Ret
     */
    public static Ret create() {
        return new Ret();
    }

    /**
     * create a ret and set status:true.
     *
     * @return Ret
     */
    public static Ret succeed() {
        return new Ret().setSucceed();
    }

    /**
     * create a succeed ret and set data:data.
     *
     * @param data data
     * @return Ret
     */
    public static Ret succeed(Object data) {
        return succeed().setData(data);
    }

    /**
     * create a ret and set status:false.
     *
     * @return Ret
     */
    public static Ret fail() {
        return new Ret().setFail();
    }

    /**
     * create a fail ret and set msg:message.
     *
     * @param msg message
     * @return Ret
     */
    public static Ret fail(Object msg) {
        return fail().set(MSG, msg);
    }

    /**
     * set status:true.
     *
     * @return Ret
     */
    public Ret setSucceed() {
        super.put(STATUS, Boolean.TRUE);
        return this;
    }

    /**
     * set status:fail.
     *
     * @return Ret
     */
    public Ret setFail() {
        super.put(STATUS, Boolean.FALSE);
        return this;
    }

    /**
     * get data as T.
     *
     * @param <T> type
     * @return Ret
     */
    public <T> T getData() {
        return (T) get(DATA);
    }

    /**
     * set data:data.
     *
     * @param data data
     * @return Ret
     */
    public Ret setData(Object data) {
        return set(DATA, data);
    }

    /**
     * judge whether the ret is succeed.
     *
     * @return is succeed
     */
    public boolean isSucceed() {
        Boolean isOk = (Boolean) get(STATUS);
        return isOk != null && isOk;
    }

    /**
     * set a pair of k,v into a ret.
     *
     * @param key   key
     * @param value value
     * @return Ret
     */
    public Ret set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * add a map into a ret.
     *
     * @param map map
     * @return Ret
     */
    public Ret set(Map map) {
        super.putAll(map);
        return this;
    }

    /**
     * add a ret into a ret.
     *
     * @param ret Ret
     * @return Ret
     */
    public Ret set(Ret ret) {
        super.putAll(ret);
        return this;
    }

    /**
     * remove k,v from map.
     *
     * @param key key
     * @return Ret
     */
    public Ret delete(Object key) {
        super.remove(key);
        return this;
    }

    /**
     * get value as a given type.
     *
     * @param key key
     * @param <T> type
     * @return value as type
     */
    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    /**
     * get value as string.
     *
     * @param key key
     * @return String
     */
    public String getStr(Object key) {
        return (String) get(key);
    }

    /**
     * get value as integer.
     *
     * @param key key
     * @return integer
     */
    public Integer getInt(Object key) {
        return (Integer) get(key);
    }

    /**
     * get value as long.
     *
     * @param key key
     * @return long
     */
    public Long getLong(Object key) {
        return (Long) get(key);
    }

    /**
     * get value as boolean.
     *
     * @param key key
     * @return boolean
     */
    public Boolean getBoolean(Object key) {
        return (Boolean) get(key);
    }

    /**
     * key exist and its value is not null then return true.
     *
     * @param key key
     * @return boolean
     */
    public boolean notNull(Object key) {
        return get(key) != null;
    }

    /**
     * key does not exist or key exist but its value is null then return true.
     *
     * @param key key
     * @return boolean
     */
    public boolean isNull(Object key) {
        return get(key) == null;
    }

    /**
     * key exist and value is true then return true.
     *
     * @param key key
     * @return boolean
     */
    public boolean isTrue(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value));
    }

    /**
     * key exist and value is false then return true.
     *
     * @param key key
     * @return boolean
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && (!((Boolean) value)));
    }

    /**
     * parse to json.
     *
     * @return json string
     */
    public String toJson() {
        return JsonKit.toJson(this);
    }

}