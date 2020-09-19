package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjComment;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WjCommentMapper extends BasicsAdminMapper<WjComment> {
    Map<String,String> querySql(@Param("sql") String sql);
    int updataSql(@Param("sql") String sql);
}