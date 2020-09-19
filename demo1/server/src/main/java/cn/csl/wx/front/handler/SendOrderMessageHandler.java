package cn.csl.wx.front.handler;

import cn.csl.basics.param.Global;
import com.alibaba.fastjson.JSONObject;
import cn.csl.wx.front.model.WxTemplateMessage;
import cn.csl.wx.util.HttpUtil;

public class SendOrderMessageHandler {

	
	public void sendOrderNoticeMessage(WxTemplateMessage orderNoticeMessage,String accessToken){
		String url =  Global.getConfig("sendMessageUrl");//WxParam.sendMessageUrl
		url =url.replace("ACCESS_TOKEN", accessToken);
		String outDataString = JSONObject.toJSONString(orderNoticeMessage);
		JSONObject resultObject = HttpUtil.httpsRequest(url, "POST", outDataString);
		System.out.println(resultObject);
	}

	/**
	 * 发送订单通知消息
	 * @param touser
	 * @param templateId
	 * @param pagepath
	 * @param accessToken
	 */
	public void sendOrderNoticeMessage(String touser, String templateId,
			String projectappid, String pagepath,String accessToken) {
		WxTemplateMessage templateMessage = new WxTemplateMessage();
		templateMessage.setTemplate_id(templateId);
		templateMessage.setTouser(touser);
		String url = Global.getConfig("sendMessageUrl");//WxParam.sendMessageUrl
		url =url.replace("ACCESS_TOKEN", accessToken);
		String outDataString = JSONObject.toJSONString(templateMessage);
		JSONObject resultObject = HttpUtil.httpsRequest(url, "POST", outDataString);
		System.out.println(resultObject);
	}
	
	public static void main(String[] args) {
	}
}
