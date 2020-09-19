package cn.csl.manage.service;

import cn.csl.manage.dao.SysEnvironmentMapper;
import cn.csl.manage.entity.*;
import cn.csl.basics.model.ResultResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysEnvironmentServive{
	@Resource
	private SysEnvironmentMapper sysEnvironmentMapper;

	public SysEnvironment load(){
		return sysEnvironmentMapper.selectByPrimaryKey(1L);
	}
	public ResultResponse editSave(SysEnvironment sysEnvironment){
		ResultResponse response = new ResultResponse();
		SysEnvironment dbSysEnvironment = sysEnvironmentMapper.selectByPrimaryKey(1L);
		dbSysEnvironment.setName(sysEnvironment.getName());
		dbSysEnvironment.setEmailHost(sysEnvironment.getEmailHost());
		dbSysEnvironment.setEmailPassword(sysEnvironment.getEmailPassword());
		dbSysEnvironment.setEmailSender(sysEnvironment.getEmailSender());
		dbSysEnvironment.setIntroduceVideoUrl(sysEnvironment.getIntroduceVideoUrl());
		sysEnvironmentMapper.updateByPrimaryKey(dbSysEnvironment);
		response.setSuccessResult("修改成功");
		return response;
	}
}
