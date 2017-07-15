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

import cn.enbug.shop.common.plugin.openSearch.OpenSearch;
import com.jfinal.kit.PropKit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.0
 * @since 1.0.0
 */
public class OpenSearchService {

    /**
     * singleton
     */
    public static final OpenSearchService ME = new OpenSearchService();
    private static final String INDEX_NAME = PropKit.get("openSearcher.appName");
    private static final String TABLE_NAME = "main";

    private OpenSearchService() {

    }

    /**
     * push
     *
     * @param data OpenSearchPushRequestBuilder.build
     * @return String
     */
    public boolean push(String data) {
        return OpenSearch.push(INDEX_NAME, TABLE_NAME, data);
    }

    /**
     * add
     *
     * @param fields hash map
     * @return String
     */
    public boolean add(HashMap<String, Object> fields) {
        return OpenSearch.add(INDEX_NAME, TABLE_NAME, fields);
    }

    /**
     * update
     *
     * @param fields hash map
     * @return String
     */
    public boolean update(HashMap<String, Object> fields) {
        return OpenSearch.update(INDEX_NAME, TABLE_NAME, fields);
    }

    /**
     * search
     *
     * @param index            index type
     * @param keyWord          key word
     * @param filter           filter
     * @param sortKey          sort key
     * @param positiveSequence positive sequence
     * @return ArrayList
     */
    public ArrayList search(String index, String keyWord, String filter, String sortKey, boolean positiveSequence) {
        return OpenSearch.search(INDEX_NAME, index, keyWord, filter, sortKey, positiveSequence);
    }

    /**
     * search
     *
     * @param index            index type
     * @param keyWord          key word
     * @param sortKey          sort key
     * @param positiveSequence positive sequence
     * @return ArrayList
     */
    public ArrayList search(String index, String keyWord, String sortKey, boolean positiveSequence) {
        return OpenSearch.search(INDEX_NAME, index, keyWord, sortKey, positiveSequence);
    }

    /**
     * search
     *
     * @param index   index type
     * @param keyWord key word
     * @param filter  filter
     * @return ArrayList
     */
    public ArrayList search(String index, String keyWord, String filter) {
        return OpenSearch.search(INDEX_NAME, index, keyWord, filter);
    }

    /**
     * search
     *
     * @param index   index type
     * @param keyWord key word
     * @return ArrayList
     */
    public ArrayList search(String index, String keyWord) {
        return OpenSearch.search(INDEX_NAME, index, keyWord);
    }

    /**
     * search suggestions
     *
     * @param suggestName suggest name
     * @param keyWord     key word
     * @return ArrayList&lt;Map&lt;"suggestions",value&gt;
     */
    public String suggest(String suggestName, String keyWord) {
        return OpenSearch.suggest(INDEX_NAME, suggestName, keyWord);
    }

}
