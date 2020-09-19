package cn.csl.basics.util.annotation.excludes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LoadExcludeAnnotationAttribute {
    public static String[] getObjectValue(Class<?> clz){
        // 拿到该类
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();//该类的所有属性
        String[] resultStrs =new String[fields.length];
        int i=0;
        for (Field field : fields) {
            Annotation annotation;
            ExcludeAnnotation excludeAnnotation = field.getAnnotation(ExcludeAnnotation.class);//该属性的SearchAnnotation
            if(excludeAnnotation!=null){
                resultStrs[i] = field.getName();
                i++;
            }
        }
        return resultStrs;
    }
}
