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

package cn.enbug.shop.common.kit;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.0
 * @since 1.0.0
 */
public class Base64Kit {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private Base64Kit() {

    }

    /**
     * 编码
     *
     * @param value byte数组
     * @return {String}
     */
    public static String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encode(String value) {
        byte[] val = value.getBytes(UTF_8);
        return Base64.getEncoder().encodeToString(val);
    }

    /**
     * 编码
     *
     * @param value       字符串
     * @param charsetName charSet
     * @return {String}
     */
    public static String encode(String value, String charsetName) {
        byte[] val = value.getBytes(Charset.forName(charsetName));
        return Base64.getEncoder().encodeToString(val);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {byte[]}
     */
    public static byte[] decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decodeToStr(String value) {
        byte[] decodedValue = Base64.getDecoder().decode(value);
        return new String(decodedValue, UTF_8);
    }

    /**
     * 解码
     *
     * @param value       字符串
     * @param charsetName 字符集
     * @return {String}
     */
    public static String decodeToStr(String value, String charsetName) {
        byte[] decodedValue = Base64.getDecoder().decode(value);
        return new String(decodedValue, Charset.forName(charsetName));
    }

}
