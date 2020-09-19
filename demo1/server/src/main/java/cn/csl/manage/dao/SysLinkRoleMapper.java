package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysLinkRole;
import cn.csl.manage.model.SysSubRole;

import java.util.List;

public interface SysLinkRoleMapper extends BasicsAdminMapper<SysLinkRole> {
    int deleteByRoleId(Long roleId);
    List<SysSubRole> listAllSubscriberRole();
}