package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysRole;

import java.util.List;

public interface SysRoleMapper extends BasicsAdminMapper<SysRole> {
    List<SysRole> listRoles();
}