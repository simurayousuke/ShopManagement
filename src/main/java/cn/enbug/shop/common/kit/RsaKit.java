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

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Yang Zhizhuang
 * @version 1.0.0
 * @since 1.0.0
 */
public class RsaKit {

    private static final String KEY_ALGORITHM = "RSA";

    private static String bytesToString(byte[] encrytpByte) {
        String result = "";
        for (Byte bytes : encrytpByte) {
            result += (char) bytes.intValue();
        }
        return result;
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
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return Base64Kit.encode(cipher.doFinal(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return bytesToString(cipher.doFinal(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * generate rsa key pair.
     *
     * @param keySize key size
     * @return KeyPair
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        return keyPairGen.generateKeyPair();
    }

    /**
     * get key from string.
     *
     * @param key string
     * @return key
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static RSAPublicKey restorePublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Kit.decode(key));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
        return (RSAPublicKey) publicKey;
    }

    /**
     * get key from string.
     *
     * @param key string
     * @return key
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static RSAPrivateKey restorePrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Kit.decode(key));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
        return (RSAPrivateKey) privateKey;
    }

    /**
     * generate key pair to file.
     *
     * @param keySize key size
     * @throws IOException              IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static void generateToFile(int keySize) throws IOException, NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair(keySize);
        FileWriter fw = new FileWriter("rsa_public.key");
        fw.write(Base64Kit.encode(keyPair.getPublic().getEncoded()));
        fw.close();
        fw = new FileWriter("rsa_private.key");
        fw.write(Base64Kit.encode(keyPair.getPrivate().getEncoded()));
        fw.close();
    }

    /**
     * get key from file.
     *
     * @param path key file path
     * @return key
     * @throws IOException              IOException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static RSAPublicKey getPublicKeyFromFile(String path) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        StringBuilder key = new StringBuilder();
        String tempString;
        while ((tempString = reader.readLine()) != null) {
            key.append(tempString);
        }
        reader.close();
        return restorePublicKey(key.toString());
    }

    /**
     * get key from file.
     *
     * @param path key file path
     * @return key
     * @throws IOException              IOException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static RSAPrivateKey getPrivateKeyFromFile(String path) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        StringBuilder key = new StringBuilder();
        String tempString;
        while ((tempString = reader.readLine()) != null) {
            key.append(tempString);
        }
        reader.close();
        return restorePrivateKey(key.toString());
    }

}
