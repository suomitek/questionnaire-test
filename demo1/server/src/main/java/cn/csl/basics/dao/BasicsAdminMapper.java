package cn.csl.basics.dao;

import cn.csl.basics.util.TableUploadUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasicsAdminMapper<T> {
    int insert(T record);
    int updateByPrimaryKey(T record);
    int deleteByPrimaryKey(Long id);
    T selectByPrimaryKey(Long id);
    List<T> listByAttribute(@Param("attribute") String attribute, @Param("checkVal") String checkVal, @Param("state") Integer state);
    List<T> listByAttrValueMap(@Param("attrValueMap") String attrValueMap, @Param("state") Integer state);
    Long getCount(@Param("tableUploadUtil") TableUploadUtil tableUploadUtil);
    List<T> listPageObjs(@Param("tableUploadUtil") TableUploadUtil tableUploadUtil);
}
