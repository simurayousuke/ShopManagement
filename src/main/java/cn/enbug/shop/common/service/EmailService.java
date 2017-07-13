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

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.bean.Email;
import cn.enbug.shop.common.builder.SingleSendMailRequestBuilder;
import cn.enbug.shop.common.kit.RedisKit;
import cn.enbug.shop.common.model.User;
import cn.enbug.shop.login.LoginService;
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
 * @author Hu Wenqiang
 * @version 1.0.8
 * @see com.aliyuncs.dm
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class EmailService {

    public static final EmailService ME = new EmailService();
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private static final String REGION = PropKit.get("mailSender.region");
    private static final String KEY = RsaService.ME.decrypt(PropKit.get("mailSender.key"));
    private static final String SECRET = RsaService.ME.decrypt(PropKit.get("mailSender.secret"));
    private static final UserService USER_SRV = UserService.ME;

    private EmailService() {
        // singleton
    }

    /**
     * send email.
     *
     * @param email Email Object
     * @param tag   tag defined at aliyun
     * @return boolean
     */
    public boolean send(Email email, String tag) {
        IClientProfile profile = DefaultProfile.getProfile(REGION, KEY, SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequestBuilder(email, tag).build();
        try {
            client.getAcsResponse(request);
        } catch (ClientException e) {
            LOG.error(e.getErrMsg(), e);
            return false;
        }
        return true;
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
        return null != RedisKit.getEmailAddressByActiveCode(code);
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

    public void bindEmailAddressForUserWithoutUpdate(User user, String emailAddress) {
        user.setEmail(emailAddress).setEmailStatus(0);
    }

    /**
     * bind email address for username.
     *
     * @param username     username
     * @param emailAddress email address
     * @return boolean
     */
    public boolean bindEmailAddressForUsername(String username, String emailAddress) {
        User user =USER_SRV.findUserByUsername(username);
        return bindEmailAddressForUser(user, emailAddress);
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
        return validateActiveCodeForEmail(code) && activeEmailAddressForUser(user);
    }

    /**
     * active email address for username
     *
     * @param username username
     * @return boolean
     */
    private boolean activeEmailAddressForUsername(String username) {
        return activeEmailAddressForUser(USER_SRV.findUserByUsername(username));
    }

    /**
     * validate then active email address for username
     *
     * @param username username
     * @param code     active code
     * @return boolean
     */
    public boolean validateThenActiveEmailAddressForUsername(String username, String code) {
        User user = USER_SRV.findUserByUsername(username);
        return validateThenActiveEmailAddressForUser(user, code);
    }

}
