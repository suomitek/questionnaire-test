package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.*;
import cn.csl.manage.admin.vo.RoleVo;
import cn.csl.manage.admin.vo.SysFrontlinkVo;
import cn.csl.manage.dao.SysFrontlinkMapper;
import cn.csl.manage.dao.SysFrontroleMapper;
import cn.csl.manage.dao.SysLinkRoleMapper;
import cn.csl.manage.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysPartServive extends BasicsService<SysFrontrole> {
	@Resource
	private SysFrontroleMapper sysFrontroleMapper;
	@Resource
	private SysFrontlinkMapper sysFrontlinkMapper;
	@Resource
	private SysLinkRoleMapper sysLinkRoleMapper;

	@Override
	public BasicsAdminMapper<SysFrontrole> getBasicsAdminMapper(){
		return  sysFrontroleMapper;
	}

	@Transactional
	public ResultResponse changeUserRole(String roleId,String linkIds){
		ResultResponse response = new ResultResponse();
		String links[] = linkIds.split(",");
		SysLinkRole sysLinkRole;
		sysLinkRoleMapper.deleteByRoleId(Long.valueOf(roleId));
		for(String link:links){
			sysLinkRole = new SysLinkRole();
			sysLinkRole.setRoleId(Long.valueOf(roleId));
			sysLinkRole.setLinkId(Long.valueOf(link));
			sysLinkRole.setState(1);
			sysLinkRoleMapper.insert(sysLinkRole);
		}
		response.setSuccessResult("修改成功");
		return response;
	}

	public List<SysFrontlinkVo> listAllUrls(String id){
		List<SysFrontlinkVo> sysFrontlinkVos = new ArrayList<>();
		List<SysFrontlink> sysFrontlinks = sysFrontlinkMapper.listByAttribute("state","1",1);
		List<SysLinkRole> sysLinkRoles = sysLinkRoleMapper.listByAttribute("roleId",String.valueOf(id),1);
		List<Long> linkIds = new ArrayList<>();
		for (SysLinkRole sysLinkRole:sysLinkRoles) {
			linkIds.add(sysLinkRole.getLinkId());
		}
		for (SysFrontlink sysFrontlink:sysFrontlinks) {
			SysFrontlinkVo sysFrontlinkVo = new SysFrontlinkVo(sysFrontlink);
			sysFrontlinkVo.setFlag(false);
			if (linkIds.contains(sysFrontlink.getId())){
				sysFrontlinkVo.setFlag(true);
			}
			sysFrontlinkVos.add(sysFrontlinkVo);
		}
		return sysFrontlinkVos;
	}

	public ResultResponse editSave(SysFrontrole sysFrontrole){
		ResultResponse response = checkInfo(sysFrontrole);
		if (response.isError()) {
			return response;
		}
		SysFrontrole dbSysRole=getBasicsAdminMapper().selectByPrimaryKey(sysFrontrole.getId());
		dbSysRole.setRoleName(sysFrontrole.getRoleName());
		dbSysRole.setRoleDesc(sysFrontrole.getRoleDesc());
		getBasicsAdminMapper().updateByPrimaryKey(dbSysRole);
		response.setSuccessResult("修改成功");
		return response;
	}
}
