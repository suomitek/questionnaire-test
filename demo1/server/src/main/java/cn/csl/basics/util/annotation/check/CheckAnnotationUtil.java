package cn.csl.basics.util.annotation.check;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAnnotationUtil<T> {
    private T pojo;

    public CheckAnnotationUtil(T pojo) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.pojo = pojo;
    }

    public CheckValueResult CheckValue (){
        CheckValueResult checkValueResult=null;
        Map<String,String> typeMethodMap = new HashMap<>();
        typeMethodMap.put("required","isNotNull");
        typeMethodMap.put("email","isEmail");
        typeMethodMap.put("url","isUrl");
        typeMethodMap.put("isMobile","isMobile");
        typeMethodMap.put("digits","isDigits");
        typeMethodMap.put("password","isPwd");
        typeMethodMap.put("number","isFloat");
        try {
        	// 拿到该类
            Class<?> clz = pojo.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();//该类的所有属性
            Class<?> regexUtilCls = RegexUtil.class;
            List<String> sList = new ArrayList<>();
            for (Field field : fields) {
                CheckAnnotation checkAnnotation = (CheckAnnotation)field.getAnnotation(CheckAnnotation.class);
                field.setAccessible(true);
                if (checkAnnotation !=null){
//                    Class<?> valueType = field.getType();
                    Class<?> valueType = String.class;
                    String attribute = checkAnnotation.attribute();
                    String message =checkAnnotation.message();
                    Method method = regexUtilCls.getMethod(typeMethodMap.get(checkAnnotation.type()),valueType);
                    method.setAccessible(true);
                    boolean flag= (boolean)method.invoke(new RegexUtil(),field.get(pojo)+"");
                    if(!flag){
                        message = "请输入正确的"+attribute;
                        checkValueResult= new CheckValueResult(field.getName(),message);
                        return checkValueResult;
                    }

                }
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
        return checkValueResult;
    }

}
