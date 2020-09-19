package cn.csl.basics.util;

import java.security.SecureRandom;

import org.apache.commons.codec.digest.DigestUtils;

public class SecureUtil {
	
	/**
	 * 获得随机加密盐，
	 * @return
	 */
	public static String getSecureRandomStr(){
		SecureRandom ranGen = new SecureRandom();
        byte[] aesKey = new byte[8];
        ranGen.nextBytes(aesKey);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < aesKey.length; i++) {
            String hex = Integer.toHexString(0xff & aesKey[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        System.out.println(hexString);
        return hexString.toString();
	}
	
    /**
     * sha256加密算法
     * @param str
     * @return
     */
    public static String sha256Hex(String str){
    	return DigestUtils.sha256Hex(str);
    }
    
    /**
     * md5加密算法
     * @param str
     * @return
     */
    public static String md5Hex(String str){
    	return DigestUtils.md5Hex(str);
    }
    /**
     * md5加密算法
     * @param str
     * @return
     */
    public static String sha1Hex(String str){
    	return DigestUtils.sha1Hex(str);
    }
	public static void main(String[] args) {
//		getSecureRandomStr();
		System.out.println(md5Hex(md5Hex("222")));
		
	}

}
