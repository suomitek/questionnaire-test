package cn.csl.wenjuan.front.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.wenjuan.entity.WjBanner;
import cn.csl.wenjuan.entity.WjCheck;
import cn.csl.wenjuan.service.WjBannerServive;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping({"/wenjuan/front/banner"})
public class FrontWjBannerController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjBannerServive jzBannerServive;

    public WjBannerServive getBasicsService() {
        return jzBannerServive;
    }



    @RequestMapping({"/listJson"})
    @ResponseBody
    public ResultResponse listJson(){
        ResultResponse response = new ResultResponse();
        try{
            List<WjBanner> banners = this.getBasicsService().listByAttribute("state","1");
            response.setSuccessResult(banners);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("查询失败");
        }
        return response;
    }
}
