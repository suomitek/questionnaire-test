package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.wenjuan.dao.WjBannerMapper;
import cn.csl.wenjuan.entity.WjBanner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WjBannerServive extends BasicsService<WjBanner> {
	@Resource
	private WjBannerMapper wjBannerMapper;

	@Override
	public BasicsAdminMapper<WjBanner> getBasicsAdminMapper(){return wjBannerMapper;}


	public List<WjBanner> listBanners(String val){
		List<WjBanner> banners = wjBannerMapper.listBanners(val);
		return banners;
 	}
	@Override
	public ResultResponse editSave(WjBanner banner){
		ResultResponse resultResponse;
		resultResponse = checkInfo(banner);
		if (resultResponse.isError()) {
			return resultResponse;
		}

		WjBanner dbBdBanner = getBasicsAdminMapper().selectByPrimaryKey(banner.getId());
		if("".equals(banner.getLinkUrl().trim())){
			dbBdBanner.setBannerType(0);
		}else{
			dbBdBanner.setBannerType(1);
		}
		dbBdBanner.setImagePath(banner.getImagePath());
		dbBdBanner.setOrderNum(banner.getOrderNum());
		dbBdBanner.setTitle(banner.getTitle());
		dbBdBanner.setLinkUrl(banner.getLinkUrl());
		getBasicsAdminMapper().updateByPrimaryKey(dbBdBanner);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
