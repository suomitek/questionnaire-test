package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.SysFrontlinkMapper;
import cn.csl.manage.entity.SysFrontlink;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUrlServive extends BasicsService<SysFrontlink> {
	@Resource
	private SysFrontlinkMapper sysFrontlinkMapper;

	@Override
	public BasicsAdminMapper<SysFrontlink> getBasicsAdminMapper(){return sysFrontlinkMapper;}


	@Override
	public ResultResponse editSave(SysFrontlink sysFrontlink){
		ResultResponse resultResponse;
		resultResponse = checkInfo(sysFrontlink);
		if (resultResponse.isError()) {
			return resultResponse;
		}

		SysFrontlink dbObj = getBasicsAdminMapper().selectByPrimaryKey(sysFrontlink.getId());
		dbObj.setLinkUrl(sysFrontlink.getLinkUrl());
		dbObj.setLinkName(sysFrontlink.getLinkName());
		dbObj.setLinkCode(sysFrontlink.getLinkCode());
		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
