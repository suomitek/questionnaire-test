package cn.csl.manage.dao;

import cn.csl.manage.entity.SysEnvironment;

public interface SysEnvironmentMapper {
    int insert(SysEnvironment record);
    int updateByPrimaryKey(SysEnvironment record);
    SysEnvironment selectByPrimaryKey(Long id);
}
