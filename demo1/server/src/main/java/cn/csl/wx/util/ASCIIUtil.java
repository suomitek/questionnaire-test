package cn.csl.wx.util;

public class ASCIIUtil {

	public static String stringToAscii(String value)  {  
        StringBuffer sbu = new StringBuffer();  
        char[] chars = value.toCharArray();   
        for (int i = 0; i < chars.length; i++) {  
            if(i != chars.length - 1)  
            {  
                sbu.append((int)chars[i]).append(",");  
            }  
            else {  
                sbu.append((int)chars[i]);  
            }  
        }  
        return sbu.toString();  
	}
	
	public static String asciiToString(String value){
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
	
	public static void main(String[] args) {
		System.out.println(stringToAscii("http://www.anxunchina.cn/ddd/ddss/merchantId/ddddx23412"));
	}
}
