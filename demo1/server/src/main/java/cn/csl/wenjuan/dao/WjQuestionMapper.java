package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjQuestionMapper extends BasicsAdminMapper<WjQuestion>{
    int delete(@Param("id")Long id);
    List<WjQuestion> listJoin(@Param("wxUserId") Long wxUserId);
    List<WjQuestion> listFootprint(@Param("wxUserId") Long wxUserId);
}