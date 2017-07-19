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

import cn.enbug.shop.common.controller.BaseController;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.upload.UploadFile;

import java.io.File;

/**
 * 上传文件Controller
 *
 * @author Hu Wenqiang
 * @version 1.0.2
 * @since 1.0.0
 */
@Before(UploadValidator.class)
public class UploadController extends BaseController {

    private static final UploadService UPLOAD_SRV = UploadService.ME;

    @Before(POST.class)
    public void index() {
        String folder = getPara();
        UploadFile uploadFile = getFile();
        File file = uploadFile.getFile();
        User user = RedisKit.getUserByToken(getCookie(RedisKit.COOKIE_ID));
        String ip = getIp();
        renderJson(UPLOAD_SRV.upload(file, user, ip, folder));
    }

}
