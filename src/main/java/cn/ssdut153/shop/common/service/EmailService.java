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
import cn.ssdut153.shop.common.kit.RedisKit;
import cn.ssdut153.shop.common.model.User;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The service for sending email.
 *
 * @author Yang Zhizhuang
 * @version 1.0.2
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
            client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(e.getErrMsg(), e);
        }
        return false;
    }

    // todo 记录操作到数据库中

    /**
     * generate and get active code for email.
     *
     * @param emailAddress email address
     * @return active code
     */
    public String generateActiveCodeForEmail(String emailAddress) {
        return RedisKit.setActiveCodeForEmailAndGet(emailAddress);
    }

    /**
     * validate active code with email.
     *
     * @param emailAddress email address
     * @param code         active code
     * @return boolean
     */
    public boolean validateActiveCodeWithEmail(String emailAddress, String code) {
        return emailAddress.equals(RedisKit.getEmailAddressByActiveCode(code));
    }

    /**
     * validate active code for email.
     *
     * @param code active code
     * @return boolean
     */
    public boolean validateActiveCodeForEmail(String code) {
        return RedisKit.getEmailAddressByActiveCode(code) != null;
    }

    /**
     * bind email address for user.
     *
     * @param user         User Object
     * @param emailAddress email address
     * @return boolean
     */
    public boolean bindEmailAddressForUser(User user, String emailAddress) {
        return user.setEmail(emailAddress).setEmailStatus(0).update();
    }

    /**
     * bind email address for username.
     *
     * @param username     username
     * @param emailAddress email address
     * @return boolean
     */
    public boolean bindEmailAddressForUsername(String username, String emailAddress) {
        return bindEmailAddressForUser(UserService.getInstance().findUserByUsername(username), emailAddress);
    }

    /**
     * active email address for user
     *
     * @param user User Object
     * @return boolean
     */
    private boolean activeEmailAddressForUser(User user) {
        if (StrKit.isBlank(user.getEmail())) {
            return false;
        }
        return user.setEmailStatus(1).update();
    }

    /**
     * validate and then active email address for user
     *
     * @param user User Object
     * @param code active code
     * @return boolean
     */
    public boolean validateThenActiveEmailAddressForUser(User user, String code) {
        if (!validateActiveCodeForEmail(code)) {
            return false;
        }
        return activeEmailAddressForUser(user);
    }

    /**
     * active email address for username
     *
     * @param username username
     * @return boolean
     */
    private boolean activeEmailAddressForUsername(String username) {
        return activeEmailAddressForUser(UserService.getInstance().findUserByUsername(username));
    }

    /**
     * validate then active email address for username
     *
     * @param username username
     * @param code     active code
     * @return boolean
     */
    public boolean validateThenActiveEmailAddressForUsername(String username, String code) {
        return validateThenActiveEmailAddressForUser(UserService.getInstance().findUserByUsername(username), code);
    }

}
