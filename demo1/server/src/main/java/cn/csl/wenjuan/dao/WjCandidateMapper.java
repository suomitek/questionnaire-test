package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjCandidate;
import org.apache.ibatis.annotations.Param;

public interface WjCandidateMapper extends BasicsAdminMapper<WjCandidate> {
    int deleteByVoteId(@Param("voteId")Long voteId);
}