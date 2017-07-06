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

import com.taobao.api.ApiException;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Builder of AlibabaAliqinFcSmsNumSendRequest.
 *
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class AlibabaAliqinFcSmsNumSendRequestBuilder {

    AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();

    public AlibabaAliqinFcSmsNumSendRequestBuilder(String name,String operation,String code,String number,String extend) {
        req.setExtend(extend);
        req.setSmsType("normal");
        req.setSmsFreeSignName("庄云");
        req.setSmsParamString("{\"name\":\"" + name + "\",\"platform\":\"庄云网\",\"operation\":\"" + operation + "\",\"code\":\"" + code + "\"}");
        req.setRecNum(number);
        req.setSmsTemplateCode("SMS_63950001");
    }

    public AlibabaAliqinFcSmsNumSendRequestBuilder(String name,String operation,String code,String number) {
        req.setSmsType("normal");
        req.setSmsFreeSignName("庄云");
        req.setSmsParamString("{\"name\":\"" + name + "\",\"platform\":\"庄云网\",\"operation\":\"" + operation + "\",\"code\":\"" + code + "\"}");
        req.setRecNum(number);
        req.setSmsTemplateCode("SMS_63950001");
    }

    public AlibabaAliqinFcSmsNumSendRequest build(){
        return req;
    }
}
