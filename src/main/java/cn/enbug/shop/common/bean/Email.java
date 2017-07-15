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

package cn.enbug.shop.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Email to send.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class Email {

    private static final String TO = "to";
    private static final String TITLE = "title";
    private static final String CONTEXT = "text";
    private Map<String, String> map = new HashMap<>();

    /**
     * constructor
     *
     * @param to      email address
     * @param title   title
     * @param context context
     */
    public Email(String to, String title, String context) {
        map.put(TO, to);
        map.put(TITLE, title);
        map.put(CONTEXT, context);
    }

    public String getTo() {
        return map.get(TO);
    }

    public void setTo(String to) {
        map.put(TO, to);
    }

    public String getTitle() {
        return map.get(TITLE);
    }

    public void setTitle(String title) {
        map.put(TITLE, title);
    }

    public String getContext() {
        return map.get(CONTEXT);
    }

    public void setContext(String context) {
        map.put(CONTEXT, context);
    }

}
