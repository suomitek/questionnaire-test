package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.SysApiMapper;
import cn.csl.manage.entity.SysApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysApiServive extends BasicsService<SysApi> {
	@Resource
	private SysApiMapper sysApiMapper;

	@Override
	public BasicsAdminMapper<SysApi> getBasicsAdminMapper(){return sysApiMapper;}


	@Override
	public ResultResponse editSave(SysApi obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}

		SysApi dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());
		dbObj.setResUrl(obj.getResUrl());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
