package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.SysResourceRoleMapper;
import cn.csl.manage.dao.SysRoleMapper;
import cn.csl.manage.dao.SysUserRoleMapper;
import cn.csl.manage.entity.SysRole;
import cn.csl.manage.entity.SysUserRole;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.admin.vo.RoleVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServive extends BasicsService<SysRole> {
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	@Resource
	private SysResourceRoleMapper sysResourceRoleMapper;

	@Override
	public BasicsAdminMapper<SysRole> getBasicsAdminMapper(){
		return  sysRoleMapper;
	}

	public List<RoleVo> distribution(String userId){
		List<RoleVo> roleVos = new ArrayList<>();
		List<SysUserRole> sysUserRoles = sysUserRoleMapper.listByAttribute("userId",userId,1);
		List<Long> roles=new ArrayList<>();
		for (SysUserRole sysUserRole:sysUserRoles){
			roles.add(sysUserRole.getRoleId());
		}
		List<SysRole> sysRoles = sysRoleMapper.listRoles();
		for (SysRole sysRole:sysRoles){
			roleVos.add(new RoleVo(sysRole,roles.contains(sysRole.getId())));
		}
		return roleVos;
	}
	@Transactional
	public ResultResponse changeUserRole(String userId,String rolesStr){
		ResultResponse response = new ResultResponse();
		String roles[] = rolesStr.split(",");
		SysUserRole sysUserRole;
		sysUserRoleMapper.deleteByUserId(Long.valueOf(userId));
		for(String role:roles){
			sysUserRole = new SysUserRole();
			sysUserRole.setRoleId(Long.valueOf(role));
			sysUserRole.setUserId(Long.valueOf(userId));
			sysUserRole.setState(1);
			sysUserRoleMapper.insert(sysUserRole);
		}
		response.setSuccessResult("修改成功");
		return response;
	}
	/**
	 * 删除角色删除用户与角色关联
	 * @param roleId
	 * @return
	 */
	@Transactional
	public ResultResponse delete(String roleId){
		ResultResponse response = super.delete(Long.valueOf(roleId));
		if (response.isError()) {
			return response;
		}
		List<SysUserRole> sysUserRoles =sysUserRoleMapper.listRolesByRoleId(Long.valueOf(roleId));
		for (SysUserRole sysUserRole:sysUserRoles) {
			sysUserRole.setState(0);
			sysUserRoleMapper.updateByPrimaryKey(sysUserRole);
		}
		sysResourceRoleMapper.deleteByRoleId(Long.valueOf(roleId));
		response.setSuccessResult("删除成功");
		return response;
	}

	public ResultResponse editSave(SysRole sysRole){
		ResultResponse response = checkInfo(sysRole);
		if (response.isError()) {
			return response;
		}
		SysRole dbSysRole=sysRoleMapper.selectByPrimaryKey(sysRole.getId());
		dbSysRole.setRoleName(sysRole.getRoleName());
		dbSysRole.setRoleDesc(sysRole.getRoleDesc());
		sysRoleMapper.updateByPrimaryKey(dbSysRole);
		response.setSuccessResult("修改成功");
		return response;
	}
}
