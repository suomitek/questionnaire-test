package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjVoterecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjVoterecordMapper extends BasicsAdminMapper<WjVoterecord> {
    List<Long> listWxUserIdByIpAddressVoteId(@Param("ipAddress")String ipAddress,@Param("voteId") Long voteId);
    List<WjVoterecord> listDayRecordByWxUserId(@Param("wxUserId") Long wxUserId);
    int deleteByVoteId(@Param("voteId")Long voteId);
}