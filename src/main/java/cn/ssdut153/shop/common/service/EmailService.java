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

package cn.ssdut153.shop.common.service;

import cn.ssdut153.shop.common.bean.Email;
import cn.ssdut153.shop.common.builder.SingleSendMailRequestBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jfinal.kit.PropKit;


/**
 * The service for sending email.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmailService {

    private static EmailService instance = new EmailService();

    private EmailService() {
    }

    public static EmailService getInstance() {
        return instance;
    }

    public boolean send(Email email, String tag) {
        IClientProfile profile = DefaultProfile.getProfile(PropKit.get("mailSender.region"), PropKit.get("mailSender.key"), PropKit.get("mailSender.secret"));
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequestBuilder(email, tag).build();
        try {
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

}
