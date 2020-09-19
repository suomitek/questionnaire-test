package cn.csl.manage.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.UserSession;
import cn.csl.basics.param.GlobalParam;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.*;
import cn.csl.manage.entity.*;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.manage.admin.vo.SysUserVo;
import cn.csl.manage.redis.JedisUtil;
import cn.csl.manage.shiro.type.LoginType;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.csl.basics.model.ResultResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysUserServive extends BasicsService<SysUser> {
	@Autowired
	private JedisUtil jedisUtil;
	@Resource
	private SysApiMapper sysApiMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysResourceMapper sysResourceMapper;
	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	@Resource
	private SysResourceRoleMapper sysResourceRoleMapper;

	@Override
	public BasicsAdminMapper<SysUser> getBasicsAdminMapper(){
		return  sysUserMapper;
	}

	public List<SysUser> adminList(){
		return sysUserMapper.listAllAdmin();
	}


	@Transactional
	public ResultResponse addSave(SysUser user){
		user.setUse(1);
		ResultResponse response = super.addSave(user);
		this.editSave(user);
		return response;
	}

	public ResultResponse changeUseFlag(String userId){
		ResultResponse response = new ResultResponse();
		SysUser sysUser = this.sysUserMapper.selectByPrimaryKey(Long.valueOf(userId));
		sysUser.setUse((sysUser.getUseFlag()+1)%2);
		this.sysUserMapper.updateByPrimaryKey(sysUser);
		response.setSuccessResult("修改成功");
		return response;
	}

	public ResultResponse editSave(SysUser user){
		ResultResponse response = checkInfo(user);
		if (response.isError()) {
			return response;
		}
		SysUser dbUser=sysUserMapper.selectByPrimaryKey(user.getId());
		dbUser.setName(user.getName());
		dbUser.setNickName(user.getNickName());
		dbUser.setPassword(new Sha256Hash(user.getPassword()).toHex());
		dbUser.setMailbox(user.getMailbox());
		dbUser.setPortraitUrl(user.getPortraitUrl());
		dbUser.setUserType(user.getUserType());
		sysUserMapper.updateByPrimaryKey(dbUser);
		response.setSuccessResult("修改成功");
		return response;
	}

	public List<SysRole> listRoles(){
		List<SysRole> roles = sysRoleMapper.listRoles();
		return roles;
	}

	public TableReturnUtil listJson(TableUploadUtil tableUploadUtil){
		TableReturnUtil tableReturnUtil = super.listJson(tableUploadUtil);
		List<SysUser> sysUsers = (List<SysUser> )tableReturnUtil.getData();
		List<SysUserVo> sysUserVos = new ArrayList<>();
		for (SysUser sysUser:sysUsers) {
			sysUserVos.add(new SysUserVo(sysUser));
		}
		tableReturnUtil.setData(sysUserVos);
		return tableReturnUtil;
	}

	public List<SysRole> listRolesByUserId(Long userId){
		List<SysUserRole> userRoles = sysUserRoleMapper.listRolesByUserId(userId);
		List<SysRole> roles=new ArrayList<SysRole>();
		for (SysUserRole sysUserRoles:userRoles) {
			SysRole role = sysRoleMapper.selectByPrimaryKey(sysUserRoles.getRoleId());
			if (role!=null){
				roles.add(role);
			}
		}
		return roles;
	}

	public void setSession(UserSession us){
		String key = "session_"+ LoginType.ADMIN.toString()+"_"+us.getUserName()+"_"+us.getUserPassword();
		List<SysRole> roleList = null;
		List<SysResource> resourceList = null;
		Set<String> permissionSet = new HashSet<>();
		Set<String> roleSet = new HashSet<>();


		roleList = this.listRolesByUserId(Long.valueOf(us.getUserId()));
		for (SysRole role:roleList) {
			roleSet.add(role.getRoleName().toString());
		}
		/**
		 * 获取用户所有权限
		 */
		resourceList = this.listUserResources(roleList);
		for(SysResource resource:resourceList){
			if(resource.getResUrl()!=null){
				if(resource.getBtnFlag()==1){
					List<SysApi> sysApis = sysApiMapper.listByAttribute("resourceId",String.valueOf(resource.getId()),1);
					for (SysApi sysApi:sysApis){
						permissionSet.add(sysApi.getResUrl());
					}
				}
				permissionSet.add(resource.getResUrl());
			}
		}
		permissionSet.add("/manage/admin/index");
		permissionSet.add("/manage/admin/welcome");
		permissionSet.add("/manage/front/introduceVideo");
		for (String permission:permissionSet){//查询完放入缓存
			jedisUtil.SETS.sadd(key+"_permissionSet",permission);
		}
		for (String role:roleSet){//查询完放入缓存
			jedisUtil.SETS.sadd(key+"_roleSet",role);
		}
		jedisUtil.expire(key+"_roleSet",60*15);//15分钟过期
		jedisUtil.expire(key+"permissionSet",60*15);//15分钟过期
	}

	public List<SysResource> listUserResources(List<SysRole> roleList){
		List<SysResource> sysResources = new ArrayList<SysResource>();
		for (SysRole sysRole:roleList) {
			List<SysResourceRole> sysResourceRoles = sysResourceRoleMapper.listResourceRoleByRoleId(sysRole.getId());
			for (SysResourceRole sysResourceRole:
				 sysResourceRoles) {
				SysResource sysResource = sysResourceMapper.selectByPrimaryKey(sysResourceRole.getResourceId());
				sysResources.add(sysResource);
			}
		}
		return sysResources;
	}
}
