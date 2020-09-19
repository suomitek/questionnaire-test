package cn.csl.basics.util.annotation.status;


import cn.csl.basics.param.StatusParam;
import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.check.CheckValueResult;
import cn.csl.basics.util.annotation.check.RegexUtil;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusAnnotationUtil {

    public static Map<String,Map<String,String>> getAttributeStatus (Class<?> clz){
        Map<String,Map<String,String>> statusListMap = Maps.newHashMap();
        Map<String,String> map = new HashMap<>();
        try {
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();//该类的所有属性
            Class<?> regexUtilCls = RegexUtil.class;
            List<String> sList = new ArrayList<>();
            for (Field field : fields) {
                StatusAnnotation statusAnnotation = (StatusAnnotation)field.getAnnotation(StatusAnnotation.class);
                field.setAccessible(true);
                if (statusAnnotation !=null){
                    boolean flag = statusAnnotation.flag();
                    if(flag==true){
                        map = StatusParam.getAttribute(field.getName());
                        statusListMap.put(field.getName(),map);
                    }
                }
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
        return statusListMap;
    }

}
