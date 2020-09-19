package cn.csl.wx.front.model;



/**
 * 小程序消息提醒
 * @author Administrator
 */
public class WxMessageBodayData {
	
	private String value;
	private String color;
	
	public WxMessageBodayData(){
		
	}
	
	public WxMessageBodayData(String value,String color){
		this.value = value;
		this.color = color;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
