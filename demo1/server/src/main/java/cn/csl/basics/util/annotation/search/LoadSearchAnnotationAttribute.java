package cn.csl.basics.util.annotation.search;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LoadSearchAnnotationAttribute {
    public static Map<String,String> getObjectValue(Class<?> clz){
        Map<String,String> resultMap = new HashMap<String,String>();
        // 拿到该类
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();//该类的所有属性
        for (Field field : fields) {
            Annotation annotation;
            SearchAnnotation searchAnnotation = field.getAnnotation(SearchAnnotation.class);//该属性的SearchAnnotation
            if(searchAnnotation!=null){
                resultMap.put(field.getName(),searchAnnotation.searchMsg());
            }
        }
        return resultMap;
    }
}
