package com.liuyong.licensetesting.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 加解密工具类
 *
 * @author ly
 * @date 2020年 07月13日 17:10:47
 */
public class RSAEncryptUtil {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        //genKeyPair();
        //加密字符串
        String message = "GX3PLzJCddr1DaudYlgZa5SWxUsEXf9LgIKE3icTg4C3Ne2hjO1J0hXDisyt/zGzN+HnNVtYUfJ9FQPR42KerIoSygBrUxU3mVlhak7wVf1nDPCJhMbC7oUARiRo3wgGucdRIJQbSikXOa35dRHlLAhXnQmFLKd5kiFciID8CPlGVNsALjptr7mTTHyHilPQgZTmG1WDrycJUSuRQToC9i6kYN5qsGbc/WBmjC1qp9RefsISCI7eqz4/IYcjG1lSDm23lIlYZQ+OT8asSGZVRtb1qsIS48yNKqa67ncE4UIwL16a97eydAoaQXgwYq9laKmKfscNEs+4Qn+gDKrrmg==";
        //System.out.println("随机生成的公钥为:" + keyMap.get(0));
        //System.out.println("随机生成的私钥为:" + keyMap.get(1));
        //String messageEn = encrypt(message, keyMap.get(0));
        //System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(message, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJM7MtPJayVkY4JNhVnvSQ6m30vypYAnvdHkMVa6RN6f8qhfcP6HR0l0yYEO6S7NYfQ5ip48bQybx5y9SWrCRGhuo8JTEkHTA/hhRe9LXt4Tc+3UDlZVaZ//6PTtj9XVHaD2ygYNYAV0Te6ZuLoUrePKfHie/cmpMLy82k5wTncbAgMBAAECgYACWGsS3J6MVfEP3lRFfn5XcWkVOKRSSk/HzC7NBuKZ+UpQTFTseRM1pFqhZ69nQ3ZQAaqnoqbKEKE5afqFbNrSGLNh7LsxgYo+Mlg4azY9CEKRDG/3ry31ARXetIWZ/zFdtCAfEBT1ZZT/2uFKGr6E+AB0BE+RBTEoZNt9BhVvAQJBAPcT5xNdbAawZdVaz2LomzU8A5noLUrip9J7IHspNEj93fURXL+9ah0ONYcJh05kTn1TiEADD94IxZbMhIYb8+ECQQCYjEUm6UCHuqAiYCXurSDoIA3IH0O7qEXmGoJzTnkjVIHnxZyAoj6Ews2zQxBv0f2iJQLR1xbJpCTBDk7siYp7AkEAktMX8TEF/d+bVDMXpF43xWW5eh83sxWHxxN2FNpCgNGjsXs005ha05NbCbkG1/XRkrYt9GQdbMNnr5ovcoZv4QJBAIDvpo9h/XH7ctg7TjRzTuoEFcH9cwGNFC+OXo5ZWJhdGEOvSj2LZK5P/Q80cxMgeXFwOPbmBtO+Ena+wkS5aDsCQCSkJThK/6pGz60H2hJPujGkQxqo063IeKjsqF7PgipPuZEwDEwl+ss6/OIPlwhqZqDHPqjom3a4E0WtZJI2dtY=");
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
        Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
        return keyMap;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptByPublic(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(encryptAndDecrypt(str.getBytes(), cipher, 117));
        return outStr;
    }

    /**
     * RSA公钥解密
     *
     * @param str       加密字符串
     * @param publicKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptpublicKey(String str, String publicKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        String outStr = new String(encryptAndDecrypt(inputByte, cipher, 128));
        return outStr;
    }

    /**
     * RSA私钥加密
     *
     * @param str        加密字符串
     * @param privateKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String privateKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        String outStr = Base64.encodeBase64String(encryptAndDecrypt(str.getBytes(), cipher, 117));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(encryptAndDecrypt(inputByte, cipher, 128));
        return outStr;
    }

    /**
     * 加密解密分段处理公共方法
     */
    private static byte[] encryptAndDecrypt(byte[] data, Cipher cipher, int maxSize) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxSize) {
                cache = cipher.doFinal(data, offSet, maxSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
}