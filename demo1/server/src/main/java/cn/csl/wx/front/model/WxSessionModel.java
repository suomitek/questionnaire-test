package cn.csl.wx.front.model;

import java.io.Serializable;

/**
 * 微信小程序通过code换取的openid和session_key
 * @author Administrator
 *
 */
public class WxSessionModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errcode;
	private String errmsg;
	private String openid;
	private String expires_in;
	private String session_key;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getSession_key() {
		return session_key;
	}
	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
