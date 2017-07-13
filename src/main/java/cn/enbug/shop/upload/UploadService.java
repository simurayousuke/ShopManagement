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

package cn.enbug.shop.upload;

import cn.enbug.shop.common.kit.Ret;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.common.service.OssService;
import com.jfinal.kit.FileKit;

import java.io.File;

/**
 * 上传文件Service
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
class UploadService {

    static final UploadService ME = new UploadService();
    private static final OssService SRV = OssService.ME;

    Ret upload(File file, User user, String ip, String folder) {
        System.out.println(file.getParent());
        System.out.println(file.getName());
        String filename = file.getName();
        String key = SRV.generateKey(folder, filename);
        String fileType = SRV.getFileType(filename);
        SRV.upload(key, filename, user.getId(), ip, fileType, file);
        FileKit.delete(file);
        return Ret.succeed().set("url", key);
    }

}
