package cn.csl.wx.front.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.csl.basics.param.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.csl.wx.util.SignatureUtil;
import cn.csl.wx.util.XMLUtils;


/**
 * 微信支付
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wxpay")
public class WxPayBackNotifyController {

	private static final Logger logger = LoggerFactory.getLogger(WxPayBackNotifyController.class);
	
	@Autowired
	private HttpSession httpSession;
	
	/**
	 * 微信支付回调函数
	 */
	@RequestMapping("/backnotify")
	public void notifyUrl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("进入回调函数");
		String resXml = "";
		InputStream ins = null;
		StringBuilder str = new StringBuilder();
		String s = "";
		try {
			//读取body数据
			ins = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins,"UTF-8"));
			while((s = br.readLine())!=null){
				str.append(s);
			}
			ins.close();
			br.close();
			//
			Map<String, Object> paramMap = new HashMap<String, Object>();
			System.out.println("获取到数据是00000000000000000"+str.toString());
			try {
				paramMap = XMLUtils.XML2Map(str.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("获取到的数据", str.toString());
			//过滤空 设置 TreeMap  
	        SortedMap<String,String> packageParams = new TreeMap<String,String>();        
	        Iterator it = paramMap.keySet().iterator();  
	        String originSign = "";
	        while (it.hasNext()) {  
	            String parameter = (String) it.next();  
	            Object parameterValue = paramMap.get(parameter);  
	            String v = "";  
	            if(null != parameterValue) {  
	                v = parameterValue.toString().trim();  
	            }  
	            packageParams.put(parameter, v); 
	            if("sign".equals(parameter) ) {  
	            	originSign = v;
	            }  
	        } 
	        System.out.println("originSign==========="+originSign);
			String sign = SignatureUtil.createSign4Pay(packageParams, Global.getConfig("mchKey"));//WxParam.mchKey
			System.out.println("sign==========="+sign);
			if(originSign.equals(sign)){
				//验证签名正确
				if("SUCCESS".equals(packageParams.get("result_code"))){  
					//本系统的订单号
					String out_trade_no = packageParams.get("out_trade_no");
					String transaction_id = packageParams.get("transaction_id");
					String payEndTime = packageParams.get("time_end");
					/**
					 * 系统逻辑
					 * 此orderId是订单的流水号
					 */
//					String orderId = out_trade_no.replace("orderId_","");
//					System.out.println("/////////////////////订单号"+orderId);
//					if(!orderSerivce.checkOrderState(orderId)){
//						System.out.println("out_trade_no=========="+out_trade_no);
//						orderSerivce.updateOrderPayState(GlobalParam.PREPARE,orderId,transaction_id,payEndTime,GlobalParam.ONLINE_PAY);
//						logger.info("支付成功");  
//					}
					//通知微信.异步确认成功.必写.
	                resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"  
	                        + "<return_msg><![CDATA[OK]]></return_msg></xml> ";  
				}else{
					logger.info("支付失败,错误信息：" + packageParams.get("err_code"));  
	                resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"  
	                        + "<return_msg><![CDATA[报文为空]]></return_msg></xml> "; 
				}
			}else{
				//验证签名失败
				logger.info("支付失败,错误信息：" + packageParams.get("err_code"));  
                resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[报文为空]]></return_msg></xml> ";  
			}
            //处理回调完成，返回结果数据  
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());  
            out.write(resXml.getBytes());  
            out.flush();  
            out.close(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("支付回调失败,错误信息：",e);  
            throw new Exception("支付回调失败");
		}
	}
	
}
