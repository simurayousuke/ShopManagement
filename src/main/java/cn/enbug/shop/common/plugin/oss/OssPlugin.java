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

package cn.enbug.shop.common.plugin.oss;

import com.jfinal.plugin.IPlugin;

/**
 * Object Storage Service Plugin
 *
 * @author Hu Wenqiang
 * @version 1.0.0
 * @since 1.0.0
 */
public class OssPlugin implements IPlugin {

    private String bucketName;
    private String endpoint;
    private String key;
    private String secret;

    public OssPlugin(String bucketName, String endpoint, String key, String secret) {
        this.bucketName = bucketName;
        this.endpoint = endpoint;
        this.key = key;
        this.secret = secret;
    }

    @Override
    public boolean start() {
        Oss.init(bucketName, endpoint, key, secret);
        return true;
    }

    @Override
    public boolean stop() {
        Oss.destroy();
        return true;
    }

}
