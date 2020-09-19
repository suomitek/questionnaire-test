package cn.csl.manage.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.service.BasicsService;
import cn.csl.manage.dao.*;
import cn.csl.manage.entity.*;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.model.ZTreeVo;
import cn.csl.manage.admin.vo.SysResourceVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysResourceServive extends BasicsService<SysResource> {
	@Resource
	private SysResourceMapper sysResourceMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	@Resource
	private SysRoleMapper sysRoleMapper;
	@Resource
	private SysResourceRoleMapper sysResourceRoleMapper;
	@Resource
	private SysApiMapper sysApiMapper;
	@Override
	public BasicsAdminMapper<SysResource> getBasicsAdminMapper(){
		return  sysResourceMapper;
	}
	@Transactional
	public ResultResponse addSave(SysResource sysResource,String apisStr){
		ResultResponse response = checkInfo(sysResource);
		if (response.isError()) {
			return response;
		}
		sysResource.setState(1);
		sysResourceMapper.insert(sysResource);
		if (sysResource.getBtnFlag()==1&& !StringUtils.isBlank(apisStr)){
			String[] apis = apisStr.split(",");
			SysApi sysApi;
			for (String api:apis) {
				sysApi = new SysApi();
				sysApi.setState(1);
				sysApi.setResUrl(sysResource.getResUrl()+api);
				sysApi.setResourceId(sysResource.getId());
				sysApiMapper.insert(sysApi);
			}
		}
		response.setSuccessResult("添加成功");
		return response;
	}

	@Transactional
	public ResultResponse delete(String id){
		ResultResponse response = new ResultResponse();
		List<SysResourceRole> sysResourceRoles = sysResourceRoleMapper.listResourceRoleByResourceId(Long.valueOf(id));
		if(sysResourceRoles.size()>0){
			response.setFailureResult("请先去角色列表中删除菜单权限");
			return response;
		}
		List<SysApi> sysApis = sysApiMapper.listByAttribute("resourceId",String.valueOf(id),1);
		for (SysApi sysApi:sysApis){
			sysApiMapper.deleteByPrimaryKey(sysApi.getId());
		}
		List<SysResource> delSysResources = new ArrayList<>();
		SysResource sysResource = sysResourceMapper.selectByPrimaryKey(Long.valueOf(id));
		delSysResources.add(sysResource);
		List<SysResource> sysResources2 = sysResourceMapper.listResourcesByParentId(sysResource.getId());
		for (SysResource sysResource2:sysResources2) {
			delSysResources.add(sysResource2);
			List<SysResource> sysResources3 = sysResourceMapper.listResourcesByParentId(sysResource2.getId());
			for (SysResource sysResource3:sysResources3) {
				delSysResources.add(sysResource3);
			}
		}
		for (SysResource delSysResource:delSysResources) {
			delSysResource.setState(0);
			sysResourceMapper.updateByPrimaryKey(delSysResource);
		}
		response.setSuccessResult("删除成功");
		return response;
	}
	@Transactional
	public ResultResponse editRoleResource(String sysRoleId,String  idsStr){
		ResultResponse response = new ResultResponse();
		sysResourceRoleMapper.deleteByRoleId(Long.valueOf(sysRoleId));
		if(StringUtils.isBlank(idsStr)){
			response.setSuccessResult("修改成功");
			return response;
		}
		String[] ids = idsStr.split(",");
		SysResourceRole sysResourceRole ;
		if(!"".equals(idsStr.trim())){
			for (int i=0;i<ids.length;i++){
				sysResourceRole= new SysResourceRole();
				sysResourceRole.setResourceId(Long.valueOf(ids[i]));
				sysResourceRole.setRoleId(Long.valueOf(sysRoleId));
				sysResourceRole.setState(1);
				sysResourceRoleMapper.insert(sysResourceRole);
			}
		}
		response.setSuccessResult("修改成功");
		return response;
	}

	public ResultResponse changeSave(SysResource sysResource,String apisStr){
		ResultResponse response = checkInfo(sysResource);
		if (response.isError()) {
			return response;
		}
		SysResource dbSysResource = super.selectByPrimaryKey(sysResource.getId());
		dbSysResource.setResCode(sysResource.getResCode());
		dbSysResource.setResName(sysResource.getResName());
		dbSysResource.setResUrl(sysResource.getResUrl());
		dbSysResource.setBtnFlag(sysResource.getBtnFlag());
		sysResourceMapper.updateByPrimaryKey(dbSysResource);

		List<SysApi> sysApis = sysApiMapper.listByAttribute("resourceId",String.valueOf(sysResource.getId()),1);
		for (SysApi sysApi:sysApis){
			sysApiMapper.deleteByPrimaryKey(sysApi.getId());
		}
		if (sysResource.getBtnFlag()==1&& !StringUtils.isBlank(apisStr)){
			String[] apis = apisStr.split(",");
			SysApi sysApi;
			for (String api:apis) {
				sysApi = new SysApi();
				sysApi.setState(1);
				sysApi.setResUrl(sysResource.getResUrl()+api);
				sysApi.setResourceId(sysResource.getId());
				sysApiMapper.insert(sysApi);
			}
		}

		response.setSuccessResult("修改成功");
		return response;
	}

	public ResultResponse editSave(SysResource sysResource){
		ResultResponse response = checkInfo(sysResource);
		response.setSuccessResult("修改成功");
		return response;
	}

	public ResultResponse loadCheckAttributeIsExistence(String attribute,String checkVal,Long checkId){
		ResultResponse response = super.loadCheckAttributeIsExistence(attribute,checkVal,checkId);
		return response;
	}
	public ResultResponse listRoleZtreeJson(String sysRoleId){
		ResultResponse response = new ResultResponse();
		List<SysResource> sysResources = new ArrayList<>();
		List<ZTreeVo> zTreeVoList = new ArrayList<>();
		ZTreeVo zTreeVo;
		List<SysResourceRole> sysResourceRoles = sysResourceRoleMapper.listResourceRoleByRoleId(Long.valueOf(sysRoleId));
		for (SysResourceRole sysResourceRole:sysResourceRoles) {
			SysResource sysResource = sysResourceMapper.selectByPrimaryKey(sysResourceRole.getResourceId());
			sysResources.add(sysResource);
		}
		List<SysResource> sysResourcesAll = sysResourceMapper.loadAll();
		for (SysResource sysResource:sysResourcesAll){
			zTreeVo = new ZTreeVo();
			zTreeVo.setId(sysResource.getId().toString());
			zTreeVo.setpId(sysResource.getParentId().toString());
			zTreeVo.setName(sysResource.getResName());
			zTreeVo.setChecked(sysResources.contains(sysResource));
			zTreeVoList.add(zTreeVo);
		}
		response.setSuccessResult(zTreeVoList);
		return response;
	}

	public List<SysResourceVo> listResources(){
		List<SysResourceVo> sysResourceVos = new ArrayList<>();
		List<SysResource> sysResources = sysResourceMapper.listResourcesByResType(1);
		for (SysResource sysResource:sysResources) {
			List<SysResource> sysChildrenResources = sysResourceMapper.listResourcesByParentId(sysResource.getId());
			sysResourceVos.add(new SysResourceVo(sysResource,sysChildrenResources));
		}
		return sysResourceVos;
	}

	public ResultResponse listResourceZTreeVoByType(int type){
		ResultResponse resultResponse = new ResultResponse();
		List<ZTreeVo> zTreeVoList = new ArrayList<>();
		ZTreeVo zTreeVo;
		List<SysResource> sysResources = sysResourceMapper.listResourceAndChildByResType(type);
		for (SysResource sysResource:sysResources) {
			zTreeVo = new ZTreeVo();
			zTreeVo.setId(sysResource.getId().toString());
			zTreeVo.setpId(sysResource.getParentId().toString());
			zTreeVo.setName(sysResource.getResName());
			zTreeVoList.add(zTreeVo);
		}

		resultResponse.setSuccessResult(zTreeVoList);
		return resultResponse;
	}

	public ResultResponse listZtreeByUserId(Long userId){
		ResultResponse resultResponse = new ResultResponse();
		List<ZTreeVo> zTreeVoList = new ArrayList<>();
		ZTreeVo zTreeVo = new ZTreeVo();
		SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
		List<SysRole> roleList = null;
		Set<SysResource> resourceList = new HashSet<>();
		//获取用户所有角色
		List<SysUserRole> userRoles = sysUserRoleMapper.listRolesByUserId(userId);
		List<SysRole> roles=new ArrayList<SysRole>();
		for (SysUserRole sysUserRoles:userRoles) {
			SysRole sysRole = sysRoleMapper.selectByPrimaryKey(sysUserRoles.getRoleId());
			if (sysRole!=null){
                zTreeVo = new ZTreeVo();
				zTreeVo.setId(sysRole.getId().toString());
				zTreeVo.setpId("0");
				zTreeVo.setName(sysRole.getRoleName());
				zTreeVoList.add(zTreeVo);
				//获取用户所有权限
				List<SysResourceRole> sysResourceRoles = sysResourceRoleMapper.listResourceRoleByRoleId(sysRole.getId());
				for (SysResourceRole sysResourceRole:
						sysResourceRoles) {
					SysResource sysResource = sysResourceMapper.selectByPrimaryKey(sysResourceRole.getResourceId());
                    zTreeVo = new ZTreeVo();
                    if(sysResource.getResType()==1){
						zTreeVo.setId(sysRole.getId()+""+sysResource.getId());
						zTreeVo.setpId(sysRole.getId().toString());
					}else if(sysResource.getResType()==2) {
						zTreeVo.setId(sysRole.getId()+""+sysResource.getParentId()+""+sysResource.getId());
						zTreeVo.setpId(sysRole.getId()+""+sysResource.getParentId());
					}else{
						SysResource sysResourceP = sysResourceMapper.selectByPrimaryKey(sysResource.getParentId());
						zTreeVo.setId(sysRole.getId()+""+sysResourceP.getParentId()+""+sysResource.getParentId()+""+sysResource.getId());
						zTreeVo.setpId(sysRole.getId()+""+sysResourceP.getParentId()+""+sysResource.getParentId());
					}
					zTreeVo.setName(sysResource.getResName());
					zTreeVoList.add(zTreeVo);
				}
			}
		}
		resultResponse.setSuccessResult(zTreeVoList);
		return resultResponse;
	}
}
