package cn.csl.wenjuan.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JSONArrayStrToArray {

    public static List<Long> dateToString(String arrayStr){
        List<Long> re = new ArrayList<>();
        if(arrayStr.length()==2){//        "[]"
            return re;
        }
//        "[1,2]"
        arrayStr = arrayStr.substring(1,arrayStr.length());
        arrayStr = arrayStr.substring(0,arrayStr.length()-1);
        String[] arr = arrayStr.split(",");
        for (String s:arr) {
            re.add(Long.valueOf(s));
        }
        return re;
    }
}
