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

package cn.enbug.shop.common.plugin.opensearch;

import cn.enbug.shop.common.exception.OpenSearchException;
import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchDoc;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.CloudsearchSuggest;
import com.aliyun.opensearch.object.KeyTypeEnum;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Open Search
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenSearch {

    private static final String STATUS = "status";
    private static final String JSON = "json";
    private static final String OK = "ok";
    private static final Logger LOG = LoggerFactory.getLogger(OpenSearch.class);
    private static CloudsearchClient client;

    private OpenSearch() {

    }

    static boolean init(String accessKey, String secret, String host, Map<String, Object> opts, KeyTypeEnum keyType) {
        try {
            client = new CloudsearchClient(accessKey, secret, host, opts, keyType);
            return true;
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    static void setMaxConnections(int maxConnections) {
        client.setMaxConnections(maxConnections);
    }

    static void destroy() {
        client = null;
    }

    /**
     * push
     *
     * @param indexName index name
     * @param tableName table name
     * @param data      OpenSearchPushRequestBuilder.build
     * @return String
     */
    public static boolean push(String indexName, String tableName, String data) {
        CloudsearchDoc doc = new CloudsearchDoc(indexName, client);
        try {
            return isSuccess(doc.push(data, tableName));
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    private static boolean isSuccess(String json) {
        HashMap map = JsonKit.parse(json, HashMap.class);
        return OK.equalsIgnoreCase(map.get(STATUS).toString());
    }

    private static List handleSearch(String json) {
        HashMap jMap = JsonKit.parse(json, HashMap.class);
        if (!OK.equalsIgnoreCase(jMap.get(STATUS).toString())) {
            return new ArrayList();
        }
        HashMap result = JsonKit.parse(jMap.get("result").toString(), HashMap.class);
        if (Integer.parseInt(result.get("num").toString()) == 0) {
            return new ArrayList();
        }
        return JsonKit.parse(result.get("items").toString(), ArrayList.class);
    }

    /**
     * add
     *
     * @param indexName index name
     * @param tableName table name
     * @param fields    hash map
     * @return String
     */
    public static boolean add(String indexName, String tableName, Map<String, Object> fields) {
        CloudsearchDoc doc = new CloudsearchDoc(indexName, client);
        doc.add(fields);
        try {
            return isSuccess(doc.push(tableName));
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * update
     *
     * @param indexName index name
     * @param tableName table name
     * @param fields    hash map
     * @return String
     */
    public static boolean update(String indexName, String tableName, Map<String, Object> fields) {
        CloudsearchDoc doc = new CloudsearchDoc(indexName, client);
        doc.update(fields);
        try {
            return isSuccess(doc.push(tableName));
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * search
     *
     * @param indexName        index name
     * @param index            index type
     * @param keyWord          key word
     * @param filter           filter
     * @param sortKey          sort key
     * @param positiveSequence positive sequence
     * @return List
     */
    public static List search(String indexName, String index, String keyWord, String filter, String sortKey, boolean positiveSequence) {
        CloudsearchSearch search = new CloudsearchSearch(client);
        // 添加指定搜索的应用：
        search.addIndex(indexName);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引到”字段。）
        if (StrKit.isBlank(index)) {
            search.setQueryString("'" + keyWord + "'");
        } else {
            search.setQueryString(index + ":'" + keyWord + "'");
        }
        // 指定搜索返回的格式。
        search.setFormat(JSON);
        // 设定过滤条件
        search.addFilter(filter);
        // 设定排序方式 + 表示正序 - 表示降序
        search.addSort(sortKey, positiveSequence ? "+" : "-");
        // 返回搜索结果
        try {
            return handleSearch(search.search());
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * search
     *
     * @param indexName        index name
     * @param index            index type
     * @param keyWord          key word
     * @param sortKey          sort key
     * @param positiveSequence positive sequence
     * @return List
     */
    public static List search(String indexName, String index, String keyWord, String sortKey, boolean positiveSequence) {
        CloudsearchSearch search = new CloudsearchSearch(client);
        // 添加指定搜索的应用：
        search.addIndex(indexName);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引到”字段。）
        if (StrKit.isBlank(index)) {
            search.setQueryString("'" + keyWord + "'");
        } else {
            search.setQueryString(index + ":'" + keyWord + "'");
        }
        // 指定搜索返回的格式。
        search.setFormat(JSON);
        // 设定排序方式 + 表示正序 - 表示降序
        search.addSort(sortKey, positiveSequence ? "+" : "-");
        // 返回搜索结果
        try {
            return handleSearch(search.search());
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * search
     *
     * @param indexName index name
     * @param index     index type
     * @param keyWord   key word
     * @param filter    filter
     * @return List
     */
    public static List search(String indexName, String index, String keyWord, String filter) {
        CloudsearchSearch search = new CloudsearchSearch(client);
        // 添加指定搜索的应用：
        search.addIndex(indexName);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引到”字段。）
        if (StrKit.isBlank(index)) {
            search.setQueryString("'" + keyWord + "'");
        } else {
            search.setQueryString(index + ":'" + keyWord + "'");
        }
        // 指定搜索返回的格式。
        search.setFormat(JSON);
        // 设定过滤条件
        search.addFilter(filter);
        // 返回搜索结果
        try {
            return handleSearch(search.search());
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * search
     *
     * @param indexName index name
     * @param index     index type
     * @param keyWord   key word
     * @return List
     */
    public static List search(String indexName, String index, String keyWord) {
        CloudsearchSearch search = new CloudsearchSearch(client);
        // 添加指定搜索的应用：
        search.addIndex(indexName);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引到”字段。）
        if (StrKit.isBlank(index)) {
            search.setQueryString("'" + keyWord + "'");
        } else {
            search.setQueryString(index + ":'" + keyWord + "'");
        }
        // 指定搜索返回的格式。
        search.setFormat(JSON);
        // 返回搜索结果
        try {
            return handleSearch(search.search());
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
    }

    /**
     * search suggestions
     *
     * @param indexName   index name
     * @param suggestName suggest name
     * @param keyWord     key word
     * @return ArrayList&lt;Map&lt;"suggestions",value&gt;
     */
    public static String suggest(String indexName, String suggestName, String keyWord) {
        CloudsearchSuggest suggest = new CloudsearchSuggest(indexName, suggestName, client);

        suggest.setHit(5);
        suggest.setQuery(keyWord);
        String result;
        try {
            result = suggest.search();
        } catch (IOException e) {
            throw new OpenSearchException(e);
        }
        HashMap jsonResult = JsonKit.parse(result, HashMap.class);
        if (jsonResult.containsKey("errors")) {
            return null;
        }
        return jsonResult.get("suggestions").toString();
        // arrayList<String,Map> --> Map<"suggestions",value>
    }

}
