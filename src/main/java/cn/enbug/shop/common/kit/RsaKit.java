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

package cn.enbug.shop.common.kit;

import cn.enbug.shop.common.exception.RsaException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Yang Zhizhuang
 * @author Hu Wenqiang
 * @version 1.1.0
 * @since 1.0.0
 */
public class RsaKit {

    private static final String KEY_ALGORITHM = "RSA";

    private RsaKit() {

    }

    private static String bytesToString(byte[] encrytpByte) {
        StringBuilder result = new StringBuilder();
        for (Byte bytes : encrytpByte) {
            result.append((char) bytes.intValue());
        }
        return result.toString();
    }

    /**
     * rsa encrypt.
     *
     * @param publicKey rsa public key
     * @param text      String
     * @return String
     */
    public static String encrypt(RSAPublicKey publicKey, String text) {

        byte[] obj = text.getBytes();

        if (null == publicKey) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64Kit.encode(cipher.doFinal(obj));
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RsaException(e);
        }

    }

    /**
     * rsa decrypt.
     *
     * @param privateKey   private key
     * @param base64String base64 string
     * @return String
     */
    public static String decrypt(RSAPrivateKey privateKey, String base64String) {

        byte[] obj = Base64Kit.decode(base64String);

        if (null == privateKey) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return bytesToString(cipher.doFinal(obj));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException
                | InvalidKeyException | IllegalBlockSizeException e) {
            throw new RsaException(e);
        }

    }

    /**
     * generate rsa key pair.
     *
     * @param keySize key size
     * @return KeyPair
     */
    public static KeyPair generateKeyPair(int keySize) {

        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(keySize);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RsaException(e);
        }

    }

    /**
     * get key from string.
     *
     * @param key string
     * @return key
     */
    public static RSAPublicKey restorePublicKey(String key) {

        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Kit.decode(key));
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return (RSAPublicKey) publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RsaException(e);
        }

    }

    /**
     * get key from string.
     *
     * @param key string
     * @return key
     */
    public static RSAPrivateKey restorePrivateKey(String key) {

        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Kit.decode(key));
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return (RSAPrivateKey) privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RsaException(e);
        }

    }

    /**
     * generate key pair to file.
     *
     * @param keySize key size
     */
    public static void generateToFile(int keySize) {

        try (FileWriter fw1 = new FileWriter("rsa_public.key");
             FileWriter fw2 = new FileWriter("rsa_private.key")) {

            KeyPair keyPair = generateKeyPair(keySize);

            fw1.write(Base64Kit.encode(keyPair.getPublic().getEncoded()));
            fw2.write(Base64Kit.encode(keyPair.getPrivate().getEncoded()));

        } catch (IOException e) {
            throw new RsaException(e);
        }

    }

    /**
     * get key from file.
     *
     * @param path key file path
     * @return key
     */
    public static RSAPublicKey getPublicKeyFromFile(String path) {

        try (FileReader fr = new FileReader(new File(path));
             BufferedReader br = new BufferedReader(fr)) {

            StringBuilder key = new StringBuilder();
            String temp;

            while (null != (temp = br.readLine())) {
                key.append(temp);
            }

            return restorePublicKey(key.toString());

        } catch (IOException e) {
            throw new RsaException(e);
        }

    }

    /**
     * get key from file.
     *
     * @param path key file path
     * @return key
     */
    public static RSAPrivateKey getPrivateKeyFromFile(String path) {

        try (FileReader fr = new FileReader(new File(path));
             BufferedReader br = new BufferedReader(fr)) {

            StringBuilder key = new StringBuilder();
            String temp;

            while (null != (temp = br.readLine())) {
                key.append(temp);
            }

            return restorePrivateKey(key.toString());

        } catch (IOException e) {
            throw new RsaException(e);
        }

    }

}
