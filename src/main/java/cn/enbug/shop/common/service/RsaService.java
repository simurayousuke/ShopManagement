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
import cn.enbug.shop.common.kit.ShortMessageKit;
import com.jfinal.kit.PropKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class RsaService {

    private static final Logger LOG = LoggerFactory.getLogger(RsaService.class);
    private static RSAPrivateKey privateKey;

    /**
     * load private key
     */
    public RsaService(){
        try {
            privateKey = RsaKit.getPrivateKeyFromFile(PropKit.get("rsa.privateKey"));
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.error(e.toString(),e);
        }
    }

    /**
     * decrypt
     *
     * @param base64 String
     * @return String
     */
    public String decrypt(String base64){
        return RsaKit.decrypt(privateKey,base64);
    }

}
