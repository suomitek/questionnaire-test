package cn.csl.manage.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.manage.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper  extends BasicsAdminMapper<SysUser> {


    int deleteByPrimaryKey(Long id);

    SysUser selectByAttribute(@Param("attribute") String attribute,@Param("checkVal") String checkVal);

    List<SysUser> listAllSysUser();
    List<SysUser> listAllAdmin();
}