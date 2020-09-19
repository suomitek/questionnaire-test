package cn.csl.wenjuan.admin.controller;

import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.search.LoadSearchAnnotationAttribute;
import cn.csl.manage.redis.JedisUtil;
import cn.csl.wenjuan.entity.WjBanner;
import cn.csl.wenjuan.service.WjBannerServive;
import cn.csl.wenjuan.service.WjQuestionServive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/wenjuan/admin/vote"})
public class AdminWjVoteController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private WjQuestionServive wjQuestionServive;
    @Resource
    private WjBannerServive wjBannerServive;

    public WjQuestionServive getBasicsService() {
        return wjQuestionServive;
    }
    @Autowired
    private JedisUtil jedisUtil;
    private String getBasicsUrl() {
        return "wenjuan/admin/vote";
    }


    @RequestMapping({"/list"})
    public String list(Model model){
        model.addAttribute("map", LoadSearchAnnotationAttribute.getObjectValue(WjBanner.class));
        model.addAttribute("url", getBasicsUrl());
        return getBasicsUrl()+"/list";
    }

    @RequestMapping({"/listJson"})
    @ResponseBody
    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil){
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        try{
            tableReturnUtil = this.getBasicsService().listJson(tableUploadUtil);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tableReturnUtil;
    }
    @RequestMapping({"/delete"})
    @ResponseBody
    public ResultResponse delete(String id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().delete(id);
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }

}
