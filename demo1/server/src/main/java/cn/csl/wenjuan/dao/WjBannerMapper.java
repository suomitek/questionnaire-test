package cn.csl.wenjuan.dao;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.wenjuan.entity.WjBanner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjBannerMapper extends BasicsAdminMapper<WjBanner> {
    List<WjBanner> listBanners(@Param("val") String val);
}