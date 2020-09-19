package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.basics.util.DatetimeUtils;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.dynamicClass.ClassUtil;
import cn.csl.wenjuan.dao.WjCommentMapper;
import cn.csl.wenjuan.dao.WjCommentrecordMapper;
import cn.csl.wenjuan.entity.WjComment;
import cn.csl.wenjuan.entity.WjCommentrecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WjCommentrecordServive extends BasicsService<WjCommentrecord> {
	@Resource
	private WjCommentrecordMapper wjCommentrecordMapper;
	@Override
	public BasicsAdminMapper<WjCommentrecord> getBasicsAdminMapper(){return wjCommentrecordMapper;}

	@Override
	@Transactional
	public ResultResponse addSave(WjCommentrecord wjCommentrecord){
		ResultResponse resultResponse = super.checkInfo(wjCommentrecord);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		String sql = " and subscriberId="+wjCommentrecord.getSubscriberId()+" and surfaceId="+wjCommentrecord.getSurfaceId()+
				" and surface='"+wjCommentrecord.getSurface()+"'"
				+" and subscriberSurface='"+wjCommentrecord.getSubscriberSurface()+"'";
		List<WjCommentrecord> olds = wjCommentrecordMapper.listByAttrValueMap(sql,1);
		String selectSql = "SELECT fabulous FROM "+wjCommentrecord.getSurface()+" where id ="+wjCommentrecord.getSurfaceId();
		Long fabulous = wjCommentrecordMapper.querySql(selectSql);
		int flag = 1;
		if(fabulous==null){//
			fabulous = 0l;
		}
		if (olds.size()>0){//删除 物理删除
			fabulous = fabulous-1;
			flag = -1;
			wjCommentrecordMapper.deleteByPrimaryKey(olds.get(0).getId());
		}else{//添加
			fabulous = fabulous+1;
			wjCommentrecord.setState(1);
			wjCommentrecord.setCreateTime(new Date());
			wjCommentrecordMapper.insert(wjCommentrecord);
		}
		String updataSql = "update "+wjCommentrecord.getSurface()+" set fabulous = "
				+fabulous+" where id ="+wjCommentrecord.getSurfaceId();
		wjCommentrecordMapper.updataSql(updataSql);
		resultResponse.setSuccessResult(flag);//返回去 当前最新的 评论数量 变化值
		return resultResponse;
	}



	@Override
	public ResultResponse editSave(WjCommentrecord obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		WjCommentrecord dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
