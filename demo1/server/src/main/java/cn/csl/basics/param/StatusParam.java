package cn.csl.basics.param;
import cn.csl.basics.param.PropertiesLoader;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class StatusParam {
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    /**
     * 保存全局属性值
     */
    private static Map<String, String> statusMap = Maps.newHashMap();

    private static Map<String,Map<String,String>> statusListMap = Maps.newHashMap();

    private static void statusMap() {
        Properties prop = new Properties();InputStream is = null;

        try{
            Resource resource = resourceLoader.getResource("status.properties");
            is = resource.getInputStream();
            prop.load(is);
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                String value = prop.getProperty(key);
                statusMap.put(key, value != null ? value : StringUtils.EMPTY);
                System.out.println(key+"===="+prop.getProperty(key));
                List<Map<String,String>> statusList = new ArrayList<>();
                String attribute = key.substring(0,key.indexOf("_"));
                Map<String,String> map = new HashMap<>();
                if(statusListMap.get(attribute)==null) {//属性不存在
                    map = new HashMap<>();
                    map.put(value.substring(0, value.indexOf(":")),value.substring(value.indexOf(":")+1, value.length()));
                }else {
                    map = statusListMap.get(attribute);
                    map.put(value.substring(0, value.indexOf(":")),value.substring(value.indexOf(":")+1, value.length()));
                }
                statusListMap.put(attribute,map);
            }
            is.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    /**
     * 获取配置 zh
     */
    public static String getConfigZh(String key) {
        String value = statusMap.get(key);
        if(value==null) {
            statusMap();
            value =statusMap.get(key);
        }
        if(value==null) {
            return null;
        }else {
            return value.substring(value.indexOf(":")+1,value.length());
        }
    }
    /**
     * 获取配置 en
     */
    public static String getConfigEn(String key) {
        String value = statusMap.get(key);
        if(value==null) {
            statusMap();
            value =statusMap.get(key);
        }
        if(value==null) {
            return null;
        }else {
            return value.substring(0,value.indexOf(":"));
        }
    }

    /**
     * 获取属性的所有状态
     * 小写
     * @param attribute
     * @return
     */
    public static Map<String,String> getAttribute(String attribute) {
        attribute = attribute.toUpperCase();
        Map<String,String> map =statusListMap.get(attribute);
        if(map==null) {
            statusMap();
            map =statusListMap.get(attribute);
        }
        return map;
    }
}

