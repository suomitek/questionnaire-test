package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjCommentrecord;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WjCommentrecordMapper extends BasicsAdminMapper<WjCommentrecord> {
    int updataSql(@Param("sql") String sql);
    Long querySql(@Param("sql") String sql);
}