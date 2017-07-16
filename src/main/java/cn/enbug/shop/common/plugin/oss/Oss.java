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

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Object Storage Service Plugin
 *
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class Oss {

    private static String bucketName;
    private static OSSClient client;

    private Oss() {

    }

    static void init(String bucketName, String endpoint, String key, String secret) {
        Oss.bucketName = bucketName;
        client = new OSSClient(endpoint, key, secret);
    }

    public static PutObjectResult upload(String key, File file) {
        return client.putObject(bucketName, key, file);
    }

    public static PutObjectResult upload(String key, InputStream input) {
        return client.putObject(bucketName, key, input);
    }

    public static InputStream download(String key) {
        OSSObject ossObject = client.getObject(bucketName, key);
        return ossObject.getObjectContent();
    }

    public static List<OSSObjectSummary> getList() {
        ObjectListing objectListing = client.listObjects(bucketName);
        return objectListing.getObjectSummaries();
    }

    public static void del(String key) {
        client.deleteObject(bucketName, key);
    }

    public static boolean isExist(String key) {
        return client.doesObjectExist(bucketName, key);
    }

    public static List<OSSObjectSummary> getFileList(String keyPrefix) {
        ObjectListing objectListing = client.listObjects(bucketName, keyPrefix);
        return objectListing.getObjectSummaries();
    }

    static void destroy() {
        client.shutdown();
        bucketName = null;
        client = null;
    }

}
