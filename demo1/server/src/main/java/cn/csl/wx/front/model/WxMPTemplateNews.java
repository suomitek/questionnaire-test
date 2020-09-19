package cn.csl.wx.front.model;

/**
 * 微信公众号
 * 模版消息体
 * @author Administrator
 *
 */
public class WxMPTemplateNews {

	private String touser;
	private String template_id;
	private String url = "";
	private Object data;
	
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
