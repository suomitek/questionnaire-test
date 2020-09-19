package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjVote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjVoteMapper extends BasicsAdminMapper<WjVote> {
    List<WjVote> listJoin(@Param("wxUserId") Long wxUserId);
    List<WjVote> listFootprint(@Param("wxUserId") Long wxUserId);
    int delete(@Param("id")Long id);
}