package cn.csl.wx.param;

import cn.csl.basics.param.Global;
import cn.csl.basics.param.PropertiesLoader;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class WxUrlParam {
    /**
     * 当前对象实例
     */
    private static Global global = new Global();
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();
    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("wxApi.properties");
    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }
    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }




//	/**
//	 * 微信小程序appid
//	 */
//	public static final String PROJECTAPPID = "wx32dc054e5d94cda4";
//	/**
//	 * 微信小程序appsecret
//	 */
//	public static final String PROJECTAPPSECRET = "eed71fcedb9c86e3b78b8080c84268a1";
//	
	
//	/**
//	 * 微信小程序appid
//	 */
//	public static final String PROJECTAPPID = "wxca771ca1c9401e3c";
//	/**
//	 * 微信小程序appsecret
//	 */	
//	public static final String PROJECTAPPSECRET = "c502d1a017c950235b5999d674c196fd";
	
//	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
//    public static String mchID = "1305867001";
//    //微信支付分配给商户的Key
//    public static String mchKey = "a95392b15a35408cbcb61c6d29100bea";
//    //支付证书路径(没用到？)
//    public static String keyPath = "/home/apache-tomcat-7.0.70/webapps/eduplat/WEB-INF/key/apiclient_cert.p12";
   
    
//    //统一下单api
//    public static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//    //支付回调地址
//  	public final static String NOTIFYURL = "https://rest.anxunchina.cn/restaurantplat/wxpay/backnotify";
//	/**
//	 * 微信小程序登录换取openid
//	 */
//	public static final String OPENIDURL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
	
//	public static String sendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
//	public static String miniProjectSendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
	
	
//	/**
//	 * 微信公众号appid
//	 */
//	public static final String MPAPPID = "wx29652abd855263f0";
//	/**
//	 * 微信公众号appsecret
//	 */
//	public static final String MPAPPSECRET = "507a37646f3d5c8ff670b2ef51f3d6b9";
//	/**
//	 * 微信公众号发送模版消息
//	 */
//	public static final String MPSENDTEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
//	
//	public static final String MPCALLWAITERMSGID_STRING = "Iy6RfaAmR4O1C7AlAUtJA9SOCE6y5GoCZbHOH2AOoNU";
	
}
