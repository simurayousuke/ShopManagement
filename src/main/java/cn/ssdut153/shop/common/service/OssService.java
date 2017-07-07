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

package cn.ssdut153.shop.common.service;

import cn.ssdut153.shop.common.model.Log;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import com.jfinal.kit.PropKit;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Object Storage Service.
 *
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @see com.aliyun.oss
 * @since 1.0.0
 */
public class OssService {

    private static final String BUCKET_NAME = "shopmanagement";

    /**
     * singleton
     */
    private static OssService instance = new OssService();

    private OSSClient client = new OSSClient(PropKit.get("ossWriter.endpoint"), PropKit.get("ossWriter.key"), PropKit.get("ossWriter.secret"));

    private OssService() {

    }

    /**
     * get OssService instance.
     *
     * @return singleton
     */
    public static OssService getInstance() {
        return instance;
    }

    /**
     * upload file to oss via InputStream.
     *
     * @param key      key
     * @param filename filename
     * @param userId   user id
     * @param ip       ip address
     * @param input    InputStream
     * @param size     file size
     * @param fileType file type
     * @return PutObjectResult
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    // todo 事务
    // todo 上传成功判断
    public PutObjectResult upload(String key, String filename, int userId, String ip, String fileType, InputStream input, long size) {
        cn.ssdut153.shop.common.model.File f = new cn.ssdut153.shop.common.model.File().setUrl(key).setFileName(filename).setSize(size).setType(fileType);
        f.save();
        Log log = new Log().setIp(ip).setOperation("upload").setUserId(userId);
        log.setJoinId(f.getId()).save();
        return client.putObject(BUCKET_NAME, key, input);
    }

    /**
     * upload file to oss via File.
     *
     * @param key      ket
     * @param filename filename
     * @param userId   user id
     * @param ip       ip address
     * @param fileType file type
     * @param file     File
     * @return PutObjectResult
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    // todo 事务
    // todo 上传成功判断
    public PutObjectResult upload(String key, String filename, int userId, String ip, String fileType, File file) {
        cn.ssdut153.shop.common.model.File f = new cn.ssdut153.shop.common.model.File().setUrl(key).setFileName(filename).setSize(file.length()).setType(fileType);
        f.save();
        Log log = new Log().setIp(ip).setOperation("upload").setUserId(userId);
        log.setJoinId(f.getId()).save();
        return client.putObject(BUCKET_NAME, key, file);
    }

    /**
     * download object from oss.
     *
     * @param key key
     * @return InputStream
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public InputStream download(String key) {
        OSSObject ossObject = client.getObject(BUCKET_NAME, key);
        return ossObject.getObjectContent();
    }

    /**
     * get first 100 files.
     *
     * @return List<OSSObjectSummary>
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public List<OSSObjectSummary> getList() {
        ObjectListing objectListing = client.listObjects(BUCKET_NAME);
        return objectListing.getObjectSummaries();
    }

    /**
     * delete file with given key.
     *
     * @param key key
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public void del(String key) {
        client.deleteObject(BUCKET_NAME, key);
    }

    /**
     * judge whether file exists.
     *
     * @param key key
     * @return boolean
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public boolean isExist(String key) {
        return client.doesObjectExist(BUCKET_NAME, key);
    }

    /**
     * get file list with given key prefix.
     *
     * @param keyPrefix key prefix
     * @return List<OSSObjectSummary>
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public List<OSSObjectSummary> getFileList(String keyPrefix) {
        ObjectListing objectListing = client.listObjects(BUCKET_NAME, keyPrefix);
        return objectListing.getObjectSummaries();
    }

    /**
     * close oss client, should not be invoked until the program exit.
     */
    public void close() {
        client.shutdown();
    }

}
