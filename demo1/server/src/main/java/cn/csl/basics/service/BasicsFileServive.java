package cn.csl.basics.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.dao.BasicsFileMapper;
import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.model.ResultResponse;
import cn.csl.manage.dao.SysApiMapper;
import cn.csl.manage.entity.SysApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BasicsFileServive extends BasicsService<BasicsFile> {
	@Resource
	private BasicsFileMapper basicsFileMapper;

	@Override
	public BasicsAdminMapper<BasicsFile> getBasicsAdminMapper(){return basicsFileMapper;}

	public ResultResponse editChange(BasicsFile basicsFile){
		ResultResponse resultResponse = checkInfo(basicsFile);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		try {
			if(basicsFile.getId()==0){//添加
				return this.addSave(basicsFile);
			}else {//不是0的情况下 有就是修改 没有继续添加
				BasicsFile file = super.selectByPrimaryKey(basicsFile.getId());
				if(file==null){
					return this.addSave(basicsFile);
				}else{
					return this.editSave(basicsFile);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			resultResponse.setFailureResult("数据异常");
			return resultResponse;
		}
	}
	@Override
	public ResultResponse addSave(BasicsFile basicsFile){
		ResultResponse resultResponse;
		resultResponse = checkInfo(basicsFile);
		if (resultResponse.isError()) {
			return resultResponse;
		}
		basicsFile.setCreateTime(new Date());
		basicsFile.setState(1);
		getBasicsAdminMapper().insert(basicsFile);
		resultResponse.setSuccessResult(basicsFile);
		return resultResponse;
	}
	@Override
	public ResultResponse editSave(BasicsFile obj){
		ResultResponse resultResponse;
		resultResponse = checkInfo(obj);
		if (resultResponse.isError()) {
			return resultResponse;
		}

		BasicsFile dbObj = getBasicsAdminMapper().selectByPrimaryKey(obj.getId());

		dbObj.setFilePath(obj.getFilePath());
		dbObj.setFileType(obj.getFileType());
		dbObj.setImgFlag(obj.getImgFlag());
		dbObj.setNature(obj.getNature());
		dbObj.setRealName(obj.getRealName());
		dbObj.setUniName(obj.getUniName());

		getBasicsAdminMapper().updateByPrimaryKey(dbObj);
		resultResponse.setSuccessResult(dbObj);
		return resultResponse;
	}
}
