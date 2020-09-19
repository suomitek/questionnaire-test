package cn.csl.wx.front.model;

import java.util.Map;

/**
 * 微信公众号
 * 模版消息体
 * @author Administrator
 *
 */
public class WxMPTemplateMessageBody {

	private Map<String, String> first;
	private Map<String, String> keyword1;
	private Map<String, String> keyword2;
	private Map<String, String> remark;
	public Map<String, String> getFirst() {
		return first;
	}
	public void setFirst(Map<String, String> first) {
		this.first = first;
	}
	public Map<String, String> getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(Map<String, String> keyword1) {
		this.keyword1 = keyword1;
	}
	public Map<String, String> getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(Map<String, String> keyword2) {
		this.keyword2 = keyword2;
	}
	public Map<String, String> getRemark() {
		return remark;
	}
	public void setRemark(Map<String, String> remark) {
		this.remark = remark;
	}
	
}
