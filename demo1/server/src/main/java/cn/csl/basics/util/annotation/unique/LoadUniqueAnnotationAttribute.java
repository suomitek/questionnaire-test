package cn.csl.basics.util.annotation.unique;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/////////////////// 只支持 string 的唯一
public class LoadUniqueAnnotationAttribute<T> {
    private T pojo;

    public LoadUniqueAnnotationAttribute(T pojo) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.pojo = pojo;
    }
    public List<UniqueResult> getUniqueAttribute(){
        List<UniqueResult> resultList = new ArrayList<>();
        try{
            // 拿到该类
            // 获取实体类的所有属性，返回Field数组
            Class<?> clz = pojo.getClass();
            Field[] fields = clz.getDeclaredFields();//该类的所有属性
            for (Field field : fields) {
                Annotation annotation;
                UniqueAnnotation searchAnnotation = field.getAnnotation(UniqueAnnotation.class);//该属性的UniqueAnnotation
                if(searchAnnotation!=null){
                    field.setAccessible(true);
                    resultList.add(new UniqueResult(field.getName(),searchAnnotation.uniqueMsg(),field.get(pojo)+""));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }
}
