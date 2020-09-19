package cn.csl.wx.service;

import cn.csl.basics.model.ResultResponse;
import cn.csl.wx.entity.WxParam;
import cn.csl.wx.dao.WxParamMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WxParamService {
    @Resource
    private WxParamMapper wxParamMapper;

    public WxParam selectByPrimaryKey(String id){
        return wxParamMapper.selectByPrimaryKey(Long.valueOf(id));
    }
    public ResultResponse addSave(WxParam wxParam){
        ResultResponse response = new ResultResponse();
        List<WxParam> wxParams = wxParamMapper.listAll();
        if(wxParams.contains(wxParam)){
            response.setFailureResult("已经存在");
            return response;
        }else {
            wxParam.setState(1);
            wxParamMapper.insert(wxParam);
        }
        response.setSuccessResult("添加成功");
        return response;
    }
    public List<WxParam> listAll(){
        return wxParamMapper.listAll();
    }
    public ResultResponse changeAccessToken(WxParam wxParam){
        ResultResponse response = new ResultResponse();
        WxParam dbWxParam = wxParamMapper.selectByPrimaryKey(wxParam.getId());
        dbWxParam.setAccessToken(wxParam.getAccessToken());
        dbWxParam.setMpAccessToken(wxParam.getMpAccessToken());
        wxParamMapper.updateByPrimaryKey(dbWxParam);
        response.setSuccessResult("修改成功");
        return response;
    }
    public ResultResponse editSave(WxParam wxParam){
        ResultResponse response = new ResultResponse();
        WxParam dbWxParam = wxParamMapper.selectByPrimaryKey(wxParam.getId());
        List<WxParam> wxParams = wxParamMapper.listAll();
        if(-1!=wxParams.indexOf(wxParam)){
            if(wxParams.get(wxParams.indexOf(wxParam)).getId()!=wxParam.getId()){
                response.setFailureResult("已经存在");
                return response;
            }
        }
        dbWxParam.setAppId(wxParam.getAppId());
        dbWxParam.setAppSecret(wxParam.getAppSecret());
        dbWxParam.setMchKey(wxParam.getMchKey());
        dbWxParam.setMchId(wxParam.getMchId());
        dbWxParam.setMpAppId(wxParam.getMpAppId());
        dbWxParam.setMpAppSecret(wxParam.getMpAppSecret());
        dbWxParam.setMpMchKey(wxParam.getMpMchKey());
        dbWxParam.setMpMchId(wxParam.getMpMchId());
        dbWxParam.setProjectType(wxParam.getProjectType());
        dbWxParam.setKeyPath(wxParam.getKeyPath());
        wxParamMapper.updateByPrimaryKey(dbWxParam);
        response.setSuccessResult("修改成功");
        return response;
    }
}
