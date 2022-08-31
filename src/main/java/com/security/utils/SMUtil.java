package com.security.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

/**
 * @BelongsProject: Springboot-Security
 * @BelongsPackage: com.security.utils
 * @Classname SMUtil
 * @Description TODO
 * @Date 2022/8/31 3:59 PM
 * @Created by wudengming
 * @Version: 1.0
 */
public class SMUtil {
    private static final String ENCODING = "UTF-8";
    private static final String KEY = "86C63180C2806ED1F47B859DE501215B";

    /**
     * @Author wudengming
     * @Description sm3算法加密-非密钥加密
     * @Date  2022/8/31
     * @Param [param]
     * @return java.lang.String
    **/
    public static String encrypt(String param) {
        String result = "";
        try {
            byte[] srcData = param.getBytes(ENCODING);
            SM3Digest digest = new SM3Digest();
            digest.update(srcData, 0, srcData.length);
            byte[] hash = new byte[digest.getDigestSize()];
            digest.doFinal(hash, 0);
            result = ByteUtils.toHexString(hash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 返回长度等于32的byte数组
     *
     * @param srcData
     * @return
     * @explain 生成对应的hash值
     */
    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * @return java.lang.String
     * @Author wudengming
     * @Description m3算法加密-指定密钥加密
     * @Date 2022/8/31
     * @Param [param, key]
     **/
    public static String encryptByKey(String param, String key) {
        byte[] paramByte = new byte[0];
        byte[] keyByte = new byte[0];
        try {
            paramByte = param.getBytes(ENCODING);
            keyByte = key.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ByteUtils.toHexString(encryptByKey(paramByte, keyByte));
    }

    /**
     * @return byte[]
     * @Author wudengming
     * @Description m3算法加密-指定密钥加密
     * @Date 2022/8/31
     * @Param [param, key]
     **/
    public static byte[] encryptByKey(byte[] param, byte[] key) {
        KeyParameter keyParameter = new KeyParameter(key);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(param, 0, param.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    /**
     * @Author wudengming
     * @Description m3算法加密-固定密钥加密
     * @Date  2022/8/31
     * @Param [param]
     * @return java.lang.String
    **/
    public static String encryptByKey(String param) {
        return encryptByKey(param, KEY);
    }

    /**
     * @Author wudengming
     * @Description 判断源数据与密钥加密数据是否一致
     * @Date  2022/8/31
     * @Param [paramBefore, paramAfter, key]
     * @return boolean
    **/
    public static boolean check(String paramBefore, String paramAfter, String key) {
        boolean flag = false;
        try {
            byte[] paramAfterByte = paramAfter.getBytes(ENCODING);
            byte[] paramBeforeByte = encryptByKey(key, paramBefore).getBytes(ENCODING);
            if (Arrays.equals(paramBeforeByte, paramAfterByte)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
