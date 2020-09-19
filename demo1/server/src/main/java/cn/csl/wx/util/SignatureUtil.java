package cn.csl.wx.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 微信生成签名工具类
 * @author Administrator
 *
 */
public class SignatureUtil {

	/**
	 * 生成预付订单签名
	 * @param parameters
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")  
    public static String createSign4Pay(SortedMap<String,String> parameters,String key){  
        StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + key);  
        System.out.println("拼接的字符串"+sb.toString());
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();  
        return sign;  
    } 
	
	/**
	 * 生成支付签名
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")  
    public static String paySign(SortedMap<String,String> parameters){  
        StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }   
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();  
        return sign;  
    } 
}
