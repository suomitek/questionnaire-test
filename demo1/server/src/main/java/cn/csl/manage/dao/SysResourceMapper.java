package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysResource;

import java.util.List;

public interface SysResourceMapper extends BasicsAdminMapper<SysResource> {
    List<SysResource> listResourcesByResType(int resType);
    List<SysResource> listResourceAndChildByResType(int resType);
    List<SysResource> listResourcesByParentId(Long parentId);
    List<SysResource> loadAll();
}