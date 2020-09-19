package cn.csl.wx.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * FileName：EncryptUtil.java                
 *             
 * Description：DES加密解密工具类
 *
 */
public class DesUtil {
    
	public static final  String key = "12345678";//密码只能为8位
    /**
     * 日志输出对象
     */
    public static final Log log = LogFactory.getLog(DesUtil.class);
    
    /**
     * 加密逻辑方法
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encryptProcess(String message) throws Exception { 
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8")); 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec); 
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8")); 
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv); 
        return cipher.doFinal(message.getBytes("UTF-8")); 
    }
    
    /**
     * 解密逻辑方法
     * @param message
     * @param key
     * @return
     * @throws Exception
     */ 
    private static String decryptProcess(String message) throws Exception { 
            byte[] bytesrc =convertHexString(message); 
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8")); 
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec); 
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8")); 
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv); 
            byte[] retByte = cipher.doFinal(bytesrc); 
            return new String(retByte); 
    }
    
    /**
     * 16进制数组数转化
     * @param ss
     * @return
     */
    private static byte[] convertHexString(String ss) throws Exception { 
        byte digest[] = new byte[ss.length() / 2]; 
        for(int i = 0; i < digest.length; i++){ 
	        String byteString = ss.substring(2 * i, 2 * i + 2); 
	        int byteValue = Integer.parseInt(byteString, 16); 
	        digest[i] = (byte)byteValue; 
        } 
        return digest; 
    }
    
    /**
     * 十六进制数转化
     * @param b
     * @return
     * @throws Exception
     */
    private static String toHexString(byte b[]) throws Exception { 
        StringBuffer hexString = new StringBuffer(); 
        for (int i = 0; i < b.length; i++) { 
            String plainText = Integer.toHexString(0xff & b[i]); 
            if (plainText.length() < 2){
            	plainText = "0" + plainText; 
            }
            hexString.append(plainText); 
        } 
           
        return hexString.toString(); 
    }
    
    /**
     * 加密方法
     */
    public static String encrypt(String message){
        String enStr = null;
        try {
             String orignStr=java.net.URLEncoder.encode(message, "utf-8"); 
             enStr=toHexString(encryptProcess(orignStr)); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enStr;
    }
    
    
    /**
     * 解密方法
     */
    public static String decrypt(String message){
        String decStr = null;
        try {
            decStr = java.net.URLDecoder.decode(decryptProcess(message), "utf-8") ;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return decStr;
    }
    
    /**
     * 测试Main方法
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception { 
         String message ="/ddd/ddss/merchantId/ddddx23412/ddddx23412"; 
         String enStr = encrypt(message);
         System.out.println("加密后:" + enStr);
         String decStr = decrypt(enStr);
         System.out.println("解密后:" + decStr);
         System.out.println(enStr.length());
         System.out.println(message.length());
    } 
}