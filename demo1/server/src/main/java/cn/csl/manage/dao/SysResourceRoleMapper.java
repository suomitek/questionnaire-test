package cn.csl.manage.dao;

import cn.csl.manage.entity.SysResourceRole;
import cn.csl.manage.model.SysUrlRole;

import java.util.List;

public interface SysResourceRoleMapper {
    int insert(SysResourceRole record);
    int deleteByRoleId(Long roleId);
    List<SysResourceRole> listResourceRoleByRoleId(Long roleId);
    List<SysResourceRole> listResourceRoleByResourceId(Long resourceId);
    List<SysUrlRole> listAllUrlRole();
}