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

package cn.enbug.shop.common.builder;

import cn.enbug.shop.common.bean.Email;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;

/**
 * @author Yang Zhizhuang
 * @version 1.0.3
 * @since 1.0.0
 */
public class SingleSendMailRequestBuilder {

    private SingleSendMailRequest req;

    public SingleSendMailRequestBuilder(Email email, String tag) {
        req = new SingleSendMailRequest();
        req.setAccountName("no-reply@mail.yangzhizhuang.net");
        req.setFromAlias("庄云网");
        req.setAddressType(1);
        req.setTagName(tag);
        req.setReplyToAddress(true);
        req.setToAddress(email.getTo());
        req.setSubject(email.getTitle());
        req.setHtmlBody(email.getContext());
    }

    public SingleSendMailRequest build() {
        return req;
    }

}
