package cn.csl.wx.util;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import cn.csl.basics.param.Global;
import cn.csl.basics.util.DatetimeUtils;
import cn.csl.basics.util.RandomUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import cn.csl.wx.front.model.WxPayInfo;

/**
 * 微信支付工具类
 * @author Administrator
 *
 */
public class WxPayUtil {

	private static final Logger logger = LoggerFactory.getLogger(WxPayUtil.class);
	
	/**
	 * 生成微信支付的支付信息
	 * @param prePayId     微信预订单id
	 * @return
	 */
	public static WxPayInfo getWxPayInfo(String prePayId){
		WxPayInfo payInfo = new WxPayInfo();
		SortedMap<String, String> payMap = new TreeMap<String,String>();
		String payTimeStamp = Long.toString(System.currentTimeMillis() / 1000);
		String payNonceStr = RandomUtil.createNonceStr(16);
		payMap.put("appId", Global.getConfig("projectAPPID"));
		payMap.put("timeStamp", payTimeStamp);
		payMap.put("nonceStr", payNonceStr);
		payMap.put("package", "prepay_id="+prePayId);
		payMap.put("signType", "MD5");
		String sign = SignatureUtil.createSign4Pay(payMap, Global.getConfig("mchKey"));//WxParam.mchKey
		payMap.put("sign", sign);
		String payJson = JSONObject.toJSONString(payMap);
		System.out.println("=========payJson=========="+payJson);
		payInfo.setPayTimeStamp(payTimeStamp);
		payInfo.setPayNonceStr(payNonceStr);
		payInfo.setPayPackage("prepay_id="+prePayId);
		payInfo.setPaySignType("MD5");
		payInfo.setPaySign(sign);
		return payInfo;
	}
	
	public static String getIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	} 
	
	/**
	 * 公众号支付
	 * 调用微信生成预订单
	 * @param openId      用户在微信中的openId
	 * @param orderNum    订单编号
	 * @param productId   产品id
	 * @param productName
	 * @param price       价格
	 * @param orderDate   订单日期
	 * @param expireDate  过期日期
	 * @param userIp
	 * @return
	 * @throws DocumentException 
	 */
	private static Map<String, Object> preOrder(String openId,String orderNum,String productId,String productName,String price,String orderDate,String expireDate,String userIp,String notifyUrl) throws DocumentException{
		SortedMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("appid", Global.getConfig("projectAPPID"));
		requestMap.put("mch_id", Global.getConfig("mchID"));//WxParam.mchID
		requestMap.put("device_info", "WEB");
		requestMap.put("nonce_str", RandomUtil.createNonceStr(16));
		requestMap.put("body", productName);
		requestMap.put("detail", productName);
		requestMap.put("attach", orderNum);
		requestMap.put("out_trade_no", orderNum);
		requestMap.put("fee_type", "CNY");
		requestMap.put("total_fee", price);
		requestMap.put("spbill_create_ip", userIp);
		requestMap.put("time_start", orderDate);
		requestMap.put("time_expire", expireDate);
		requestMap.put("goods_tag", "WXG");
		System.out.println("notifyUrl========================="+notifyUrl);
		requestMap.put("notify_url", notifyUrl);
		requestMap.put("trade_type", "JSAPI");
		requestMap.put("product_id", productId);
		requestMap.put("openid", openId);
		String sign = SignatureUtil.createSign4Pay(requestMap,Global.getConfig("mchKey"));//WxParam.mchKey
		requestMap.put("sign", sign);
		String requestXml = XMLUtils.convertToXML(requestMap);
		
		String resultString = HttpUtil.httpRequestString(Global.getConfig("unifiedorder_api"), "POST", requestXml);//WxParam.UNIFIEDORDER_API
		System.out.println("++++++++++++++++"+resultString);
		Map<String, Object> responseMap = XMLUtils.XML2Map(resultString);
		System.out.println("----------------"+responseMap);
		return responseMap;
	}
	
	/**
	 * 生成预订单
	 * @param openId
	 * @param orderId
	 * @param ipStr
	 * @return
	 */
	public static String getPreId(String openId, String orderId, String ipStr,int countPoints,String notifyUrl,String productName) {
		Date todayDate = new Date();
		Date nextDate = DatetimeUtils.addDays(todayDate, 1);
		String todayDateStr = DatetimeUtils.dateToString(todayDate, "yyyyMMddHHmmss");
		String nextDateStr = DatetimeUtils.dateToString(nextDate, "yyyyMMddHHmmss");
		String preId = "";
		try {
			Map<String, Object> resultMap = preOrder(openId, orderId,orderId, productName, String.valueOf(countPoints), todayDateStr, nextDateStr, ipStr,notifyUrl);
			if(resultMap.get("return_code")!=null&&"SUCCESS".equals(resultMap.get("return_code").toString())
					&&resultMap.get("result_code")!=null&&"SUCCESS".equals(resultMap.get("result_code").toString())){
				preId = String.valueOf(resultMap.get("prepay_id"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("请求微信统一下单接口出错",e);
		}
		return preId;
	}
}
