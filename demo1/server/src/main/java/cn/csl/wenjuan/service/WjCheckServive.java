package cn.csl.wenjuan.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsService;
import cn.csl.wenjuan.dao.WjBannerMapper;
import cn.csl.wenjuan.dao.WjCheckMapper;
import cn.csl.wenjuan.dao.WjChoiceMapper;
import cn.csl.wenjuan.entity.WjBanner;
import cn.csl.wenjuan.entity.WjCheck;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WjCheckServive extends BasicsService<WjCheck> {
	@Resource
	private WjCheckMapper wjCheckMapper;

	@Override
	public BasicsAdminMapper<WjCheck> getBasicsAdminMapper(){return wjCheckMapper;}


	@Override
	public ResultResponse editSave(WjCheck check){
		ResultResponse resultResponse;
		resultResponse = checkInfo(check);
		if (resultResponse.isError()) {
			return resultResponse;
		}

		WjCheck dbWjCheck = getBasicsAdminMapper().selectByPrimaryKey(check.getId());
		dbWjCheck.setIcon(check.getIcon());
		dbWjCheck.setName(check.getName());
		dbWjCheck.setOrderNum(check.getOrderNum());
		dbWjCheck.setReg(check.getReg());
		dbWjCheck.setMsg(check.getMsg());
		getBasicsAdminMapper().updateByPrimaryKey(dbWjCheck);
		resultResponse.setSuccessResult("修改成功");
		return resultResponse;
	}
}
