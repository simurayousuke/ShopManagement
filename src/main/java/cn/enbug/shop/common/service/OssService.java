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

import cn.enbug.shop.common.model.Log;
import cn.enbug.shop.common.plugin.oss.Oss;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectResult;
import com.jfinal.kit.StrKit;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Object Storage Service.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.6
 * @see com.aliyun.oss
 * @since 1.0.0
 */
public class OssService {

    public static final OssService ME = new OssService();

    private OssService() {
        // singleton
    }

    /**
     * generate key for oss.
     *
     * @param folder   target folder, eg: pictures (no slash)
     * @param filename filename, eg: good.png
     * @return key
     */
    public String generateKey(String folder, String filename) {
        String type = filename.substring(filename.lastIndexOf('.'));
        return folder + "/" + StrKit.getRandomUUID() + type;
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
    public PutObjectResult upload(String key, String filename, int userId,
                                  String ip, String fileType, InputStream input, long size) {
        cn.enbug.shop.common.model.File f = new cn.enbug.shop.common.model.File()
                .setUrl(key).setFileName(filename).setSize(size).setFileType(fileType);
        f.save();
        Log log = new Log().setIp(ip).setOperation("upload").setUserId(userId);
        log.setJoinId(f.getId()).save();
        return Oss.upload(key, input);
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
    public PutObjectResult upload(String key, String filename, int userId,
                                  String ip, String fileType, File file) {
        cn.enbug.shop.common.model.File f = new cn.enbug.shop.common.model.File()
                .setUrl(key).setFileName(filename).setSize(file.length()).setFileType(fileType);
        f.save();
        Log log = new Log().setIp(ip).setOperation("upload").setUserId(userId);
        log.setJoinId(f.getId()).save();
        return Oss.upload(key, file);
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
        return Oss.download(key);
    }

    /**
     * get first 100 files.
     *
     * @return List&lt;OSSObjectSummary&gt;
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public List<OSSObjectSummary> getList() {
        return Oss.getList();
    }

    /**
     * delete file with given key.
     *
     * @param key key
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public void del(String key) {
        Oss.download(key);
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
        return Oss.isExist(key);
    }

    /**
     * get file list with given key prefix.
     *
     * @param keyPrefix key prefix
     * @return List&lt;OSSObjectSummary&gt;
     * @throws OSSException    OSSException
     * @throws ClientException ClientException
     */
    public List<OSSObjectSummary> getFileList(String keyPrefix) {
        return Oss.getFileList(keyPrefix);
    }

    public String getFileType(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (-1 == index) {
            return "";
        }
        return fileName.substring(index);
    }

    public String getFileType(File file) {
        return getFileType(file.getName());
    }

}
