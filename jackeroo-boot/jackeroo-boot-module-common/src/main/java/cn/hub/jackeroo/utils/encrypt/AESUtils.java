package cn.hub.jackeroo.utils.encrypt;

import cn.hub.jackeroo.utils.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 对称算法加密/解密工具类 AES-128-ECB加密
 * @author alex
 * @date 2021/08/23
 */
public class AESUtils {

    /**
     * 固定密钥
     */
    private static final String key = "qvXs320aWA14Rdlj";

    /**
     * 加密 - 固定密钥
     * @param sSrc
     * @return
     */
    public static String encrypt(String sSrc) {
        return encrypt(sSrc, key);
    }

    /**
     * 加密 - 私有密钥
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) {
        try {
            validParam(sSrc, sKey);

            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new BASE64Encoder().encode(encrypted);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密 - 固定密钥
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        return decrypt(sSrc, key);
    }

    /**
     * 解密 - 私有密钥
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            validParam(sSrc, sKey);
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void validParam(String sSrc, String sKey) throws Exception {
        // 判断Key是否正确
        if (StringUtils.isEmpty(sSrc) || StringUtils.isEmpty(sKey)) {
            throw new Exception("内容和密钥不能为空");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new Exception("密钥不正确");
        }
    }
}
