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

package cn.enbug.shop.common.service;

import cn.enbug.shop.common.kit.RsaKit;
import com.jfinal.kit.PropKit;

import java.security.interfaces.RSAPrivateKey;

/**
 * @author Yang Zhizhuang
 * @version 1.0.2
 * @since 1.0.0
 */
public class RsaService {

    public static final RsaService ME = new RsaService();
    private static RSAPrivateKey privateKey = RsaKit.getPrivateKeyFromFile(PropKit.get("rsa.privateKey"));

    /**
     * load private key
     */
    private RsaService() {

    }

    /**
     * decrypt
     *
     * @param base64 String
     * @return String
     */
    public String decrypt(String base64) {
        return RsaKit.decrypt(privateKey, base64);
    }

}
