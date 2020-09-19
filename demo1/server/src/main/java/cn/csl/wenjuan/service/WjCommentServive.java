package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.dao.BasicsFileMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.basics.util.DatetimeUtils;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.dynamicClass.ClassUtil;
import cn.csl.wenjuan.dao.*;
import cn.csl.wenjuan.entity.*;
import cn.csl.wenjuan.front.dto.PrescribedDto;
import cn.csl.wenjuan.front.vo.WjCommentVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WjCommentServive extends BasicsService<WjComment> {
	@Resource
	private WjCommentMapper wjCommentMapper;
	@Resource
	private WjCommentrecordMapper wjCommentrecordMapper;
	@Override
	public BasicsAdminMapper<WjComment> getBasicsAdminMapper(){return wjCommentMapper;}

	@Override
	public ResultResponse addSave(WjComment comment){
		ResultResponse resultResponse;
		resultResponse = super.addSave(comment);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		//把被评论表的 被评论字段 加1
		String selectSql = "SELECT comment FROM "+comment.getSurface()+" where id ="+comment.getSurfaceId();
		Long commentNum = Long.valueOf(wjCommentMapper.querySql(selectSql).get("comment"));
		if(commentNum==null){
			commentNum = 0l;
		}
		commentNum++;
		String updataSql = "update "+comment.getSurface()+" set comment = "
				+commentNum+" where id ="+comment.getSurfaceId();
		wjCommentMapper.updataSql(updataSql);
		resultResponse.setSuccessResult(comment);
		return resultResponse;
	}

	public TableReturnUtil listJson2(TableUploadUtil tableUploadUtil,String subscriberId,String subscriberSurface) throws Exception{
		TableReturnUtil tableReturnUtil = new TableReturnUtil();
		long totalCount = this.getBasicsAdminMapper().getCount(tableUploadUtil).longValue();
		tableReturnUtil.setRecordsTotal(totalCount);
		tableReturnUtil.setRecordsFiltered(totalCount);
		Long pageNum = totalCount/tableUploadUtil.getLength();
		if(totalCount%tableUploadUtil.getLength()!=0){
			pageNum++;
		}
		tableReturnUtil.setPageNum(pageNum);
		List<WjComment> comments = this.getBasicsAdminMapper().listPageObjs(tableUploadUtil);
		List<Object> commentVos = new ArrayList<>();
		for (WjComment comment:comments){
			Map<String,String> allMap = new HashMap<>();
			Map<String,String> thisMap = new HashMap<>();
			String remarkSql="SELECT "+comment.getNature()+" from "+comment.getSurface()
					+" where id="+comment.getSurfaceId();
			thisMap = wjCommentMapper.querySql(remarkSql);
			allMap.putAll(thisMap);
			String userSql="SELECT "+comment.getSubscriberNature()+" from "+comment.getSubscriberSurface()
					+" where id="+comment.getSubscriberId();
			thisMap = wjCommentMapper.querySql(userSql);
			allMap.putAll(thisMap);
			List<WjCommentrecord> records =  wjCommentrecordMapper.listByAttrValueMap(
					" and surfaceId="+comment.getId()+" and surface='wj_comment'" + " and subscriberId="+subscriberId+
					" and subscriberSurface = '"+subscriberSurface+"'",1);
			allMap.put("mark",String.valueOf(records.size()));
			commentVos.add(get(comment,allMap));
		}
		tableReturnUtil.setData(commentVos);
		return tableReturnUtil;
	}

	private Object get(WjComment comment,Map<String,String> allMap) throws Exception{
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<String,Object> typeMap = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : allMap.entrySet()) {
			returnMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			typeMap.put(entry.getKey(), String.class);
		}
		returnMap.put("id", Long.valueOf(comment.getId()));
		typeMap.put("id", Long.class);
		returnMap.put("state", Integer.valueOf(comment.getState()));
		typeMap.put("state", Integer.class);
		returnMap.put("createTime", comment.getCreateTime());
		typeMap.put("createTime", Date.class);
		returnMap.put("createTimeStr", DatetimeUtils.dateToString(comment.getCreateTime()));
		typeMap.put("createTimeStr", String.class);
		returnMap.put("costSurfaceId",  Long.valueOf(comment.getCostSurfaceId()));
		typeMap.put("costSurfaceId", Long.class);
		returnMap.put("costNature",  String.valueOf(comment.getCostNature()));
		typeMap.put("costNature", String.class);
		returnMap.put("subscriberId",  Long.valueOf(comment.getSubscriberId()));
		typeMap.put("subscriberId", Long.class);
		returnMap.put("costSurface", String.valueOf(comment.getCostSurface()));
		typeMap.put("costSurface", String.class);
		returnMap.put("surface", String.valueOf(comment.getSurface()));
		typeMap.put("surface", String.class);
		returnMap.put("nature", comment.getNature());
		typeMap.put("nature", String.class);
		returnMap.put("subscriberSurface", String.valueOf(comment.getSubscriberSurface()));
		typeMap.put("subscriberSurface", String.class);
		returnMap.put("subscriberNature", String.valueOf(comment.getSubscriberNature()));
		typeMap.put("subscriberNature", String.class);
		returnMap.put("comment", String.valueOf(comment.getComment()));
		typeMap.put("comment", String.class);
		returnMap.put("surfaceId",  Long.valueOf(comment.getSurfaceId()));
		typeMap.put("surfaceId", Long.class);
		returnMap.put("useless", Long.valueOf(comment.getUseless()));
		typeMap.put("useless", Long.class);
		returnMap.put("fabulous",  Long.valueOf(comment.getFabulous()));
		typeMap.put("fabulous", Long.class);
		return new ClassUtil().dynamicClass(new Object(),returnMap,typeMap);
	}

	@Override
	public ResultResponse editSave(WjComment obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		WjComment dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
