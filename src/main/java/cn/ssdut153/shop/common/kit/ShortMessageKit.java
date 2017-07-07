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

package cn.ssdut153.shop.common.kit;

import cn.ssdut153.shop.common.builder.AlibabaAliqinFcSmsNumSendRequestBuilder;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The help kit for SMS.
 *
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.0.1
 * @since 1.0.0
 */
public class ShortMessageKit {

    private static final Logger log = LoggerFactory.getLogger(ShortMessageKit.class);
    /**
     * singleton
     */
    private static final ShortMessageKit me = new ShortMessageKit();
    /**
     * alidayu's client
     */
    private static TaobaoClient client = null;

    /**
     * @return singleton
     */
    public static ShortMessageKit getMe() {
        return me;
    }

    /**
     * initialize a client of alidayu.
     *
     * @param url    request url
     * @param appkey app key
     * @param secret app secret
     */
    public synchronized void init(String url, String appkey, String secret) {
        if (null == client) {
            client = new DefaultTaobaoClient(url, appkey, secret);
        }
    }

    /**
     * send short message.
     *
     * @param name      username
     * @param operation operation type
     * @param code      captcha code
     * @param number    phone number
     * @return json string
     */
    public String send(String name, String operation, String code, String number) {
        if (null == client) {
            return "";
        }
        AlibabaAliqinFcSmsNumSendRequestBuilder ab = new AlibabaAliqinFcSmsNumSendRequestBuilder(name, operation, code, number);
        AlibabaAliqinFcSmsNumSendRequest req = ab.build();
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            log.error(e.toString(), e);
        }
        if (null == rsp) {
            return "";
        } else {
            return rsp.getBody();
        }
    }

}
