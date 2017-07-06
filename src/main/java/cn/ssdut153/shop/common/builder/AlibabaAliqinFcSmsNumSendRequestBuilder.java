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

package cn.ssdut153.shop.common.builder;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;

/**
 * Builder of AlibabaAliqinFcSmsNumSendRequest.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class AlibabaAliqinFcSmsNumSendRequestBuilder {

    private AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

    public AlibabaAliqinFcSmsNumSendRequestBuilder(String name, String operation, String code, String number, String extend) {
        req.setExtend(extend);
        req.setSmsType("normal");
        req.setSmsFreeSignName("庄云");
        req.setSmsParamString("{\"name\":\"" + name + "\",\"platform\":\"庄云网\",\"operation\":\"" + operation + "\",\"code\":\"" + code + "\"}");
        req.setRecNum(number);
        req.setSmsTemplateCode("SMS_63950001");
    }

    public AlibabaAliqinFcSmsNumSendRequestBuilder(String name, String operation, String code, String number) {
        req.setSmsType("normal");
        req.setSmsFreeSignName("庄云");
        Kv kv = Kv.by("name", name)
                .set("platform", "庄云网")
                .set("operation", operation)
                .set("code", code);
        req.setSmsParamString(JsonKit.toJson(kv));
        req.setRecNum(number);
        req.setSmsTemplateCode("SMS_63950001");
    }

    public AlibabaAliqinFcSmsNumSendRequest build() {
        return req;
    }

}
