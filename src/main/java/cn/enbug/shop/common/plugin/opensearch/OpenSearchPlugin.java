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

import com.aliyun.opensearch.object.KeyTypeEnum;
import com.jfinal.plugin.IPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Open Search Plugin
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenSearchPlugin implements IPlugin {

    private String accessKey;
    private String secret;
    private String host;
    private KeyTypeEnum keyType;
    private Map<String, Object> opts = new HashMap<>();
    private Integer maxConnections;

    public OpenSearchPlugin(String accessKey, String secret, String host, KeyTypeEnum keyType) {
        this.accessKey = accessKey;
        this.secret = secret;
        this.host = host;
        this.keyType = keyType;
    }

    public void setVersion(String version) {
        opts.put("version", version);
    }

    public void setTimeout(int timeout) {
        opts.put("timeout", timeout);
    }

    public void setConnectTimeout(int connectTimeout) {
        opts.put("connect_timeout", connectTimeout);
    }

    public void setDebug(boolean debug) {
        opts.put("debug", debug);
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    @Override
    public boolean start() {
        if (!OpenSearch.init(accessKey, secret, host, opts, keyType)) {
            return false;
        }
        if (null != maxConnections) {
            OpenSearch.setMaxConnections(maxConnections);
        }
        return true;
    }

    @Override
    public boolean stop() {
        OpenSearch.destroy();
        return true;
    }

}
