package cn.csl.wx.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.csl.basics.param.Global;
import cn.csl.basics.util.DatetimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import cn.csl.wx.front.model.WxMPTemplateMessageBody;
import cn.csl.wx.front.model.WxMPTemplateNews;

/**
 * 微信公众号发送模版消息
 * @author Administrator
 *
 */
public class WxSendMessageUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WxSendMessageUtil.class);
	
	public static void sendCallWaiterMessage(String tableName,String openId,String token) {
		WxMPTemplateNews news = new WxMPTemplateNews();
		news.setTemplate_id(Global.getConfig("mpcallwaitermsgid"));//WxParam.MPCALLWAITERMSGID_STRING
		news.setTouser(openId);
		WxMPTemplateMessageBody data = new WxMPTemplateMessageBody();
		Map<String, String> first = new HashMap<String, String>();
		first.put("value", tableName+"正在呼叫");
		first.put("color", "#FF0000");
		data.setFirst(first);
		Map<String, String> keyword1 = new HashMap<String, String>();
		keyword1.put("value", tableName);
		keyword1.put("color", "#FF0000");
		data.setKeyword1(keyword1);
		Map<String, String> keyword2 = new HashMap<String, String>();
		keyword2.put("value", DatetimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		keyword2.put("color", "#173177");
		data.setKeyword2(keyword2);
		Map<String, String> remark = new HashMap<String, String>();
		remark.put("value", "请立即查看");
		remark.put("color", "#000000");
		data.setRemark(remark);
		news.setData(data);
		news.setUrl("");
		String outData = JSONObject.toJSONString(news);
		sendTemplateNews(token, outData);
	}
	
	/**
	 * 发送模版消息
	 * @param accessToken
	 * @param outData
	 * @return
	 */
	public static void sendTemplateNews(String accessToken,String outData){
		String url = Global.getConfig("mpsendtemplate");//WxParam.MPSENDTEMPLATE 
		url = url.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = HttpUtil.httpsRequest(url, "POST", outData);
		logger.error("发送模版消息结果信息", jsonObject);
	}
	
	public static void main(String[] args) {
		sendCallWaiterMessage("大厅座", "o-vFFt2ukYZYXqlDXjs4mFhOOspE", "Wu4NvQIRf8AVGfBKFzQ1jJshmJLtIRtU7ZlOlatrOe3LYWiqUSKRu3kt35OMfEJZLUQOuWnBr-eX-nYBo-Mmit6CrmpxTc0JDzusZMll5kiJUIT7ZZ3x66Hne-urHLJ9IUZdAFARDI");
	}
	
}
