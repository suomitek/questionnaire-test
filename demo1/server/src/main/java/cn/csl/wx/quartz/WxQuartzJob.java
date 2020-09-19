package cn.csl.wx.quartz;

import cn.csl.wx.service.WxParamService;
import cn.csl.wx.entity.WxParam;
import cn.csl.wx.param.WxUrlParam;
import cn.csl.wx.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class WxQuartzJob {
    @Resource
    private WxParamService wxParamService;

    public void getAccessToken(){
        System.out.println("------------------------------------------");
        System.out.println("------------------------------------------");
        System.out.println("------------------------------------------");
        List<WxParam> wxParams = wxParamService.listAll();
        for (WxParam wxParam:wxParams){
            String url =  WxUrlParam.getConfig("accessToken");
            url =url.replace("APPID", wxParam.getAppId());
            url =url.replace("SECRET", wxParam.getAppSecret());
            JSONObject resultObject = HttpUtil.httpsRequest(url, "POST", null);
            wxParam.setAccessToken(resultObject.getString("access_token"));
            System.out.println(wxParam.getAccessToken());
            wxParamService.changeAccessToken(wxParam);
        }
        System.out.println("------------------------------------------");
        System.out.println("------------------------------------------");
        System.out.println("------------------------------------------");
        System.out.println("------------------------------------------");
    }
}
