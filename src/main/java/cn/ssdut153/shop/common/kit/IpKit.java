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

import javax.servlet.http.HttpServletRequest;

/**
 * 获取 ip 地址的工具类
 *
 * @author Hu Wenqiang
 * @author Yang Zhizhuang
 * @version 1.0.1
 * @since 1.0.0
 */
public class IpKit {

    private static final String UNKNOWN = "unknown";
    private static final String[] headers=new String[]{
            "x-forwarded-for","Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","X-Real-IP"
    };

    private IpKit() {

    }

    /**
     * 获取request请求头中的ip地址
     *
     * @param request request亲贵
     * @return 获取到的ip地址
     */
    public static String getRealIp(HttpServletRequest request) {
        int i=0;
        String ip = request.getHeader(headers[i]);
        while(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)){
            if(i==headers.length-1){
                ip = request.getRemoteAddr();
                break;
            }
            ip=request.getHeader(headers[++i]);
        }
        return ip;
    }

}
