package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper extends BasicsAdminMapper<SysUserRole> {
    int insert(SysUserRole record);
    int deleteByUserId(Long userId);
    List<SysUserRole> listRolesByUserId(Long userId);
    List<SysUserRole> listRolesByRoleId(Long RoleId);
}