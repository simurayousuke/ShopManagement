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

package cn.enbug.shop.common.kit;

import cn.enbug.shop.common.exception.UrlEncodingException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class UrlKit {

    private UrlKit() {

    }

    public static String encode(String text, String enc) {
        try {
            return URLEncoder.encode(text, enc);
        } catch (UnsupportedEncodingException e) {
            throw new UrlEncodingException(e);
        }
    }

    public static String decode(String text, String enc) {
        try {
            return URLDecoder.decode(text, enc);
        } catch (UnsupportedEncodingException e) {
            throw new UrlEncodingException(e);
        }
    }

}
