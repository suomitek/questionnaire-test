package cn.csl.wenjuan.dao;

import cn.csl.wenjuan.entity.WjQuestionProblem;

public interface WjQuestionProblemMapper {
    int insert(WjQuestionProblem record);

    int insertSelective(WjQuestionProblem record);
}