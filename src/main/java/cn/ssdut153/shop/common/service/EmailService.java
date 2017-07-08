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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The service for sending email.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @see com.aliyuncs.dm
 * @since 1.0.0
 */
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    /**
     * singleton
     */
    private static EmailService instance = new EmailService();

    private EmailService() {
    }

    /**
     * get EmailService instance
     *
     * @return singleton
     */
    public static EmailService getInstance() {
        return instance;
    }

    /**
     * send email.
     *
     * @param email Email Object
     * @param tag   tag defined at aliyun
     * @return boolean
     */
    public boolean send(Email email, String tag) {
        IClientProfile profile = DefaultProfile.getProfile(PropKit.get("mailSender.region"), PropKit.get("mailSender.key"), PropKit.get("mailSender.secret"));
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequestBuilder(email, tag).build();
        try {
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(e.getErrMsg(), e);
        }
        return false;
    }

}