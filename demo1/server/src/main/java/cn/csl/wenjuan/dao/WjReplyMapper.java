package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.vo.chart.line.ChartLine;
import cn.csl.wenjuan.entity.WjReply;
import cn.csl.wenjuan.front.vo.WjReplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjReplyMapper extends BasicsAdminMapper<WjReply>{
    List<ChartLine> listChartLine(@Param("frotmt") String frotmt,@Param("questionId") Long questionId);
    List<WjReplyVo> listReplyVo(@Param("questionId") Long questionId);
    WjReplyVo selectByReplyVo(@Param("questionId") Long questionId,@Param("wxUserId") Long wxUserId);
    int deleteByQuestionId(@Param("questionId")Long questionId);
}