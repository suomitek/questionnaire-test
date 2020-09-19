package cn.csl.wenjuan.dao;

import cn.csl.wenjuan.entity.WjQuestionProblemUser;

public interface WjQuestionProblemUserMapper {
    int insert(WjQuestionProblemUser record);

    int insertSelective(WjQuestionProblemUser record);
}