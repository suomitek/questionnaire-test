package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjProblem;
import org.apache.ibatis.annotations.Param;

public interface WjProblemMapper extends BasicsAdminMapper<WjProblem>{
    int deleteByQuestionId(@Param("questionId")Long questionId);
}