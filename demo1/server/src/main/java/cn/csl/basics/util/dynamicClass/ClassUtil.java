package cn.csl.basics.util.dynamicClass;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

import cn.csl.wenjuan.front.vo.WjCommentVo;
import com.alibaba.fastjson.JSONObject;

public class ClassUtil {

    public Object dynamicClass(Object object,Map<String,Object> returnMap,Map<String,Object> typeMap) throws Exception {
        //map转换成实体对象
        DynamicBean bean = new DynamicBean(typeMap);
        //赋值
        Set keys = typeMap.keySet();
        for(Iterator it = keys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            bean.setValue(key, returnMap.get(key));
        }
        Object obj = bean.getObject();
        return obj;
    }

    public static void main(String[] args) throws Exception {
        Map<String,Object> returnMap = new HashMap<String, Object>();
        Map<String,Object> typeMap = new HashMap<String, Object>();

        returnMap.put("id", 1L);
        typeMap.put("id", Long.class);

        returnMap.put("state", 1);
        typeMap.put("state", Integer.class);

        returnMap.put("createTime", new Date());
        typeMap.put("createTime", Date.class);

        returnMap.put("costSurfaceId", 12L);
        typeMap.put("costSurfaceId", Long.class);

        returnMap.put("costNature", 11L);
        typeMap.put("costNature", Long.class);

        returnMap.put("subscriberId", 11L);
        typeMap.put("subscriberId", Long.class);

        returnMap.put("costSurface", "wj_candidate");
        typeMap.put("costSurface", String.class);

        returnMap.put("surface", "wj_candidate");
        typeMap.put("surface", String.class);

        returnMap.put("nature", "wj_candidate");
        typeMap.put("nature", String.class);

        returnMap.put("subscriberSurface", "wj_candidate");
        typeMap.put("subscriberSurface", String.class);

        returnMap.put("subscriberNature", "wj_candidate");
        typeMap.put("subscriberNature", String.class);

        returnMap.put("comment", "wj_candidate");
        typeMap.put("comment", String.class);

        returnMap.put("surfaceId", 0L);
        typeMap.put("surfaceId", Long.class);

        returnMap.put("useless", 0L);
        typeMap.put("useless", Long.class);

        returnMap.put("fabulous", 0L);
        typeMap.put("fabulous", Long.class);

        Object object = new ClassUtil().dynamicClass(new Object(),returnMap,typeMap);
        System.out.println(JSONObject.toJSON(object));
    }
}