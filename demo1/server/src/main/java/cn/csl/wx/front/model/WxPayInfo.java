package cn.csl.wx.front.model;


/**
 * 微信公众号支付页面中wx.chooseWXPay中参数信息
 * @author Administrator
 *
 */
public class WxPayInfo {
	// 支付签名时间戳
	private String payTimeStamp;
	// 支付签名随机串，不长于 32 位
	private String payNonceStr;
	 // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
	private String payPackage;
	// 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
	private String paySignType;
	// 支付签名
	private String paySign;
	private String payJson;
	private String prePayId;
	
	
	public String getPayTimeStamp() {
		return payTimeStamp;
	}
	public void setPayTimeStamp(String payTimeStamp) {
		this.payTimeStamp = payTimeStamp;
	}
	public String getPayNonceStr() {
		return payNonceStr;
	}
	public void setPayNonceStr(String payNonceStr) {
		this.payNonceStr = payNonceStr;
	}
	public String getPayPackage() {
		return payPackage;
	}
	public void setPayPackage(String payPackage) {
		this.payPackage = payPackage;
	}
	public String getPaySignType() {
		return paySignType;
	}
	public void setPaySignType(String paySignType) {
		this.paySignType = paySignType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getPayJson() {
		return payJson;
	}
	public void setPayJson(String payJson) {
		this.payJson = payJson;
	}
	public String getPrePayId() {
		return prePayId;
	}
	public void setPrePayId(String prePayId) {
		this.prePayId = prePayId;
	}
	
}
