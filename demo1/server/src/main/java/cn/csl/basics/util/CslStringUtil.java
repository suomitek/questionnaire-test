package cn.csl.basics.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CslStringUtil {

	//手机号
	/**
     * 所有号: 
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9]
     * 移动号: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     * 联通号: 130,131,132,155,156,185,186,145,176,1709
     * 电信号: 133,153,180,181,189,177,1700
     */
	public static boolean checkPhoneNum(String phoneNum){
		if(StringUtils.isBlank(phoneNum)){
			return false;
		}
		Pattern numericPattern = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0678])\\d{8}$");
		Matcher m = numericPattern.matcher(phoneNum);
		if (m.find()) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 用户名验证
	 * 规则：字母、数字及 '-' '_' '@' '.' 字符组合，长度6-30
	 * @param userName
	 * @return
	 */
	public static boolean checkUserName(String userName){
		if(StringUtils.isBlank(userName)){
			return false;
		}
		String regex = "^[0-9a-zA-Z-_@.]{6,30}$";
		Pattern numericPattern = Pattern.compile(regex);
		Matcher m = numericPattern.matcher(userName);
		if (m.find()) {
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 密码验证
	 * 规则：字母、数字及符号 字符组合，长度6-16
	 * @return
	 */
	public static boolean checkPassword(String password){
		if(StringUtils.isBlank(password)){
			return false;
		}
		String regex = "[\\w\\d^\\w^\\d]{6,16}";
		Pattern numericPattern = Pattern.compile(regex);
		Matcher m = numericPattern.matcher(password);
		if (m.find()) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断是否是正整数
	 * @param str
	 * @return
	 */
	public static boolean isPostiveInteger(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		String rex = "^\\d+$";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	/**
	 * 判断是否是正浮点数
	 * 创建日期：2016年2月20日
	 * @author rookie
	 * explain：
	 */
	public static boolean isPostiveFloat(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		String rex = "^\\d+(\\.\\d{1,2})?$";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(str);
		return m.find();
	}
	
	/**
	 * 验证密码
	 * 数字和字母 下划线
	 * 创建日期：2015年12月25日
	 * @author rookie
	 * explain：
	 */
	public static boolean checkPwd(String pwd){
		if(StringUtils.isBlank(pwd)){
			return false;
		}
		Pattern p = Pattern.compile("^\\w{5,17}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}
}

