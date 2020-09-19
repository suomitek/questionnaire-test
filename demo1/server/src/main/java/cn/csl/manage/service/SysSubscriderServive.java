package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.admin.vo.FrontRoleVo;
import cn.csl.manage.dao.*;
import cn.csl.manage.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysSubscriderServive extends BasicsService<SysSubscriber> {
	@Resource
	private SysFrontroleMapper sysFrontroleMapper;
	@Resource
	private SysFrontlinkMapper sysFrontlinkMapper;
	@Resource
	private SysLinkRoleMapper sysLinkRoleMapper;
	@Resource
	private SysSubscriberMapper sysSubscriberMapper;
	@Resource
	private SysSubscriberRoleMapper sysSubscriberRoleMapper;

	@Override
	public BasicsAdminMapper<SysSubscriber> getBasicsAdminMapper(){return sysSubscriberMapper;}


	public List<SysFrontlink> listSubLinks(List<SysFrontrole> roleList){
		List<SysFrontlink> sysFrontlinks = new ArrayList<SysFrontlink>();
		for (SysFrontrole sysFrontrole:roleList) {
			List<SysLinkRole> sysLinkRoles = sysLinkRoleMapper.listByAttribute("roleId",String.valueOf(sysFrontrole.getId()),1);
			for (SysLinkRole sysLinkRole:sysLinkRoles) {
				SysFrontlink sysFrontlink = sysFrontlinkMapper.selectByPrimaryKey(sysLinkRole.getLinkId());
				sysFrontlinks.add(sysFrontlink);
			}
		}
		return sysFrontlinks;
	}

	public List<SysFrontrole>  listRolesBySubscriderId(Long id){
		List<SysSubscriberRole> subscriberRoles = sysSubscriberRoleMapper.listByAttribute("scriberId",String.valueOf(id),1);
		List<SysFrontrole> roles=new ArrayList<SysFrontrole>();
		for (SysSubscriberRole sysSubscriberRole:subscriberRoles) {
			SysFrontrole role = sysFrontroleMapper.selectByPrimaryKey(sysSubscriberRole.getRoleId());
			if (role!=null){
				roles.add(role);
			}
		}
		return roles;
	}
	public ResultResponse changeUseFlag(String id){
		ResultResponse resultResponse = new ResultResponse();
		SysSubscriber sysSubscriber = sysSubscriberMapper.selectByPrimaryKey(Long.valueOf(id));
		sysSubscriber.setUseFlag((sysSubscriber.getUseFlag()+1)%2);
		sysSubscriberMapper.updateByPrimaryKey(sysSubscriber);
		if (sysSubscriber.getUseFlag()==0){
			resultResponse.setSuccessResult("禁用成功");
		}else {
			resultResponse.setSuccessResult("启用成功");
		}
		return resultResponse;
	}
	public List<FrontRoleVo> distribution(String userId){
		List<FrontRoleVo> roleVos = new ArrayList<>();
		List<SysSubscriberRole> sysSubRoles = sysSubscriberRoleMapper.listByAttribute("scriberId",userId,1);
		List<Long> roles=new ArrayList<>();
		for (SysSubscriberRole sysSubscriberRole:sysSubRoles){
			roles.add(sysSubscriberRole.getRoleId());
		}
		List<SysFrontrole> sysRoles = sysFrontroleMapper.listByAttribute("state","1",1);
		for (SysFrontrole role:sysRoles){
			roleVos.add(new FrontRoleVo(roles.contains(role.getId()),role));
		}
		return roleVos;
	}
	@Transactional
	public ResultResponse changeUserRole(String id,String rolesStr){
		ResultResponse response = new ResultResponse();
		sysSubscriberRoleMapper.deleteBySubscriberId(Long.valueOf(id));
		if(StringUtils.isBlank(rolesStr)){
			response.setSuccessResult("修改成功");
			return response;
		}
		String roles[] = rolesStr.split(",");
		SysSubscriberRole sysSubscriberRole;
		for(String role:roles){
			sysSubscriberRole = new SysSubscriberRole();
			sysSubscriberRole.setRoleId(Long.valueOf(role));
			sysSubscriberRole.setScriberId(Long.valueOf(id));
			sysSubscriberRole.setState(1);
			sysSubscriberRoleMapper.insert(sysSubscriberRole);
		}
		response.setSuccessResult("修改成功");
		return response;
	}
	@Override
	public ResultResponse editSave(SysSubscriber sysSubscriber){
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
