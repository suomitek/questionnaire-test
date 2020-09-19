package cn.csl.wx.service;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.CslStringUtil;
import cn.csl.manage.dao.SysSubscriberMapper;
import cn.csl.manage.entity.SysSubscriber;
import cn.csl.wx.entity.WxParam;
import cn.csl.wx.entity.WxUser;
import cn.csl.wx.dao.WxParamMapper;
import cn.csl.wx.dao.WxUserMapper;
import cn.csl.wx.param.WxUrlParam;
import cn.csl.wx.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WxUserService {
    @Resource
    private WxParamMapper wxParamMapper;
    @Resource
    private WxUserMapper wxUserMapper;
    @Resource
    private SysSubscriberMapper sysSubscriberMapper;
    /**
     * 插入 默认设置setState 为1
     * @return
     */
    public ResultResponse addSave(WxUser wxUser){
        ResultResponse response = new ResultResponse();
        WxUser dbWxUser = wxUserMapper.loadByOpenId(wxUser.getOpenid());
        if(dbWxUser==null){
            SysSubscriber sysSubscriber = new SysSubscriber();
            sysSubscriber.setUseFlag(1);
            sysSubscriber.setCreateTime(new Date());
            sysSubscriber.setName(wxUser.getNickName());
            sysSubscriber.setState(1);
            sysSubscriberMapper.insert(sysSubscriber);
            wxUser.setState(1);
            wxUser.setCreateTime(new Date());
            wxUser.setSubscriberId(sysSubscriber.getId());
            wxUserMapper.insert(wxUser);
        }else {
            wxUser=dbWxUser;
        }
        response.setSuccessResult(wxUser);
        return response;
    }
    public ResultResponse loadByOpenid(String openid){
        ResultResponse response = new ResultResponse();
        WxUser dbWxUser = wxUserMapper.loadByOpenId(openid);
        if(dbWxUser==null){
            response.setFailureResult("查询失败");
        }else {
            response.setSuccessResult(dbWxUser);
        }
        return response;
    }
    public ResultResponse getOpenId(String code){
        ResultResponse response = new ResultResponse();
        WxParam wxParam = wxParamMapper.selectByPrimaryKey(1l);
        String url = WxUrlParam.getConfig("openidurl");
        url =url.replace("APPID", wxParam.getAppId());
        url =url.replace("SECRET", wxParam.getAppSecret());
        url =url.replace("JSCODE", code);
        JSONObject resultObject = HttpUtil.httpsRequest(url, "POST", null);
        response.setSuccessResult(resultObject);
        return response;
    }
    public ResultResponse editSave(WxUser wxUser){
        ResultResponse response = new ResultResponse();
        WxUser dbWxUser = wxUserMapper.selectByPrimaryKey(wxUser.getId());
        if(!CslStringUtil.checkPhoneNum(wxUser.getPhone())){
            response.setFailureResult("手机号格式错误");
            return response;
        }
        if(CslStringUtil.checkUserName(wxUser.getRealName())){
            response.setFailureResult("用户名错误");
            return response;
        }
        dbWxUser.setPhone(wxUser.getPhone());
        dbWxUser.setRealName(wxUser.getRealName());
        wxUserMapper.updateByPrimaryKey(dbWxUser);
        response.setSuccessResult(dbWxUser);
        return response;
    }
}
