package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.dao.BasicsFileMapper;
import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.wenjuan.dao.WjCandidateMapper;
import cn.csl.wenjuan.dao.WjCommentMapper;
import cn.csl.wenjuan.dao.WjVoteMapper;
import cn.csl.wenjuan.dao.WjVoterecordMapper;
import cn.csl.wenjuan.entity.WjCandidate;
import cn.csl.wenjuan.entity.WjComment;
import cn.csl.wenjuan.entity.WjVote;
import cn.csl.wenjuan.entity.WjVoterecord;
import cn.csl.wenjuan.front.dto.PrescribedDto;
import cn.csl.wenjuan.front.vo.WjCandidateVo;
import cn.csl.wenjuan.front.vo.WjVoteVo;
import cn.csl.wenjuan.util.JSONArrayStrToArray;
import cn.csl.wx.dao.WxUserMapper;
import cn.csl.wx.entity.WxUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WjVoterecordServive extends BasicsService<WjVoterecord> {
	@Resource
	private WjVoterecordMapper wjVoterecordMapper;
	@Resource
	private WjVoteMapper wjVoteMapper;
	@Resource
	private WjCommentMapper wjCommentMapper;
	@Resource
	private WjCandidateMapper wjCandidateMapper;

	@Override
	public BasicsAdminMapper<WjVoterecord> getBasicsAdminMapper(){return wjVoterecordMapper;}

	@Transactional
	public ResultResponse addSave(PrescribedDto prescribedDto, String candidateId){
		WjCandidate candidate = wjCandidateMapper.selectByPrimaryKey(Long.valueOf(candidateId));
		WjVote vote = wjVoteMapper.selectByPrimaryKey(candidate.getVoteId());
		if(vote.getRestrictFlag()==1){//不限制地区的投票不用处理
			prescribedDto.setCity(prescribedDto.getCity().replace("市",""));
			prescribedDto.setProvince(prescribedDto.getProvince().replace("市",""));
			prescribedDto.setProvince(prescribedDto.getProvince().replace("省",""));
		}
		ResultResponse response =this.check(candidate,prescribedDto,vote);
		if (response.isError()){
			return response;
		}
		vote.setSupNum(vote.getSupNum()+1);// 投票 总票数
		candidate.setSupNum(candidate.getSupNum()+1);// 候选项 总票数
		WjVoterecord wjVoterecord = new WjVoterecord();
		try {
			BeanUtils.copyProperties(wjVoterecord, prescribedDto);//
		}catch (Exception e){
		}

		wjVoterecord.setState(1);
		wjVoterecord.setCreateTime(new Date());
		wjVoterecord.setVoteId(candidate.getVoteId());
		wjVoterecord.setCandidateId(candidate.getId());
		wjVoterecordMapper.insert(wjVoterecord);
		wjVoteMapper.updateByPrimaryKey(vote);
		wjCandidateMapper.updateByPrimaryKey(candidate);
		Map<String,Object> map = new HashMap<>();
		map.put("canFlag",response.getData());
		map.put("voterecordId",wjVoterecord.getId());
//		wjVoteMapper.updateByPrimaryKey(wjVote);//双向 再次更新
		response.setSuccessResult(map);
		return response;
	}
	private ResultResponse check(WjCandidate candidate,PrescribedDto prescribedDto,WjVote vote){
		ResultResponse response =new ResultResponse();

		//看进行状态
		if(vote.getSuspend()!=1){//不为1就是暂停
			response.setFailureResult("活动暂停");
			return response;
		}

		//看公开情况
		if(vote.getOvert()!=1&&!vote.getVoteCode().equals(prescribedDto.getCode())){//不为1就是不公开 还不相等
			response.setFailureResult("活动密码错误");
			return response;
		}

		//看时间
		Date now = new Date();
		if (now.before(vote.getStartTime())){//现在是开始之前
			response.setFailureResult("活动还没有开始");
			return response;
		}
		if (now.after(vote.getEndTime())){//现在是结束之后
			response.setFailureResult("活动已经结束");
			return response;
		}

		//是否可以评论
		int canFlag = 0;
		if(vote.getComment()==1){//开启评论
			canFlag = 1;
			List<WjComment> comments = wjCommentMapper.listByAttrValueMap("and surface= 'wj_candidate'"
					+"and nature='id'"+"and surfaceId="+candidate.getId(),1);
			if(comments.size()>0){
				canFlag=0;
			}
		}

		//投票次数
		List<WjVoterecord>  records = wjVoterecordMapper.listByAttribute("wxUserId",prescribedDto.getWxUserId(),1);
		if(vote.getRepeatFlag()==0){//不可重复 全部次数已经用完了
			if(records.size()>=vote.getFrequency()){
				response.setFailureResult("活动期间每个用户最多"+vote.getFrequency()+"票");
				return response;
			}
		}else{
			List<WjVoterecord> dayRecords = wjVoterecordMapper.listDayRecordByWxUserId(Long.valueOf(prescribedDto.getWxUserId()));
			if((dayRecords.size()>=vote.getFrequency())){//可重复 今日 次数 已经用完了
				response.setFailureResult("今日"+vote.getFrequency()+"票已用完了,改日再来吧");
				return response;
			}
		}

		if(vote.getRestrictFlag()==1){//限制 ip 地区
			//看地区
			String province = vote.getProvince();
			String city = vote.getCity();
			String district = vote.getDistrict();
			if (!province.equals(prescribedDto.getProvince())){
				response.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return response;
			}
			//城市不是不限且 和 当前城市不同
			if ((!"不限".equals(city))&&(!city.equals(prescribedDto.getCity()))){
				response.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return response;
			}
			//区县不是不限且 和 当前区县不同
			if ((!"不限".equals(district))&&(!district.equals(prescribedDto.getDistrict()))){
				response.setFailureResult("活动仅限:"+province+"-"+city+"-"+district);
				return response;
			}

			//ip限制
			List<Long> wxUserIds = wjVoterecordMapper.listWxUserIdByIpAddressVoteId(prescribedDto.getIpAddress(),vote.getId());
			// 该ip中有的微信用户数量 大于等于 限制数量 而且 自己的id不在 这个里边
			if(wxUserIds.size()>=vote.getIpWxUserFrequency()&&!wxUserIds.contains(prescribedDto.getWxUserId())){
				response.setFailureResult("该活动限制每个IP不超过"+vote.getIpWxUserFrequency()+"个微信用户");
				return response;
			}
		}
		response.setSuccessResult(canFlag);
		return response;
	}

	@Override
	public ResultResponse editSave(WjVoterecord obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		WjVoterecord dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
