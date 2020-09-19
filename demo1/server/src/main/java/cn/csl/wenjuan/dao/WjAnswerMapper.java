package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjAnswer;
import cn.csl.wenjuan.front.vo.WjAnswerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjAnswerMapper extends BasicsAdminMapper<WjAnswer>{
    List<WjAnswerVo> listAnswerVoByProblemId(@Param("problemId")Long problemId);
    List<WjAnswerVo> listAnswerVoByQuestionId(@Param("questionId")Long questionId);
    int deleteByQuestionId(@Param("questionId")Long questionId);
}