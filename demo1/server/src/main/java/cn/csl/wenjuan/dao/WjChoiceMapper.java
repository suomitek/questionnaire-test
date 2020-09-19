package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjChoice;
import org.apache.ibatis.annotations.Param;

public interface WjChoiceMapper extends BasicsAdminMapper<WjChoice>{
    int deleteByProblemId(@Param("problemId")Long problemId);
}