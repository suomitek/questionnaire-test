package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysSubscriberRole;
import cn.csl.manage.model.SysSubRole;
import cn.csl.manage.model.SysUrlRole;

import java.util.List;

public interface SysSubscriberRoleMapper extends BasicsAdminMapper<SysSubscriberRole> {
    int deleteBySubscriberId(Long scriberId);
}