package cn.csl.basics.util;

import java.text.DecimalFormat;

public class NumbStr {

	/**
	 * 得到补位的字符串
	 * @param srcInt
	 * @param pos
	 * @param covChar
	 * @return
	 */
	public static String coverPos(Integer srcInt, Integer pos, String covChar) {
		String strInt = String.valueOf(srcInt);
		while (strInt.length() < pos) {
			strInt = covChar + strInt;
		}
		return strInt;
	}

	/**
	 * <ul>
	 * <li>Description:[正确地处理整数后自动加零的情况]</li>
	 * <li>Created by [Huyvanpull] [Jan 20, 2010]</li>
	 * <li>Midified by [modifier] [modified time]</li>
	 * <ul>
	 * 
	 * @param sNum
	 * @return
	 */
	public static String getRightStr(String sNum) {
		DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
		String resultStr = decimalFormat.format(new Double(sNum));
		if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
			resultStr = resultStr.substring(0, resultStr.indexOf("."));
		}
		return resultStr;
	}
	/**
	 * string强转number
	 * @param str
	 * @return
	 */
	public static Integer StrToNumb(String str){
		Integer i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			i = 0;
		}
		return i;
	}
	
	/**
	 * 保留小数位数
	 * format #.#
	 * @return
	 */
	public static String getDecimalFormat(String format,double val){
		DecimalFormat df = new DecimalFormat(format);
        return df.format(val);
	}

}
