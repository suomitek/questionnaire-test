package cn.csl.basics.controller;

import cn.csl.basics.entity.BasicsFile;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.service.BasicsFileServive;
import cn.csl.basics.util.RandomUtil;
import cn.csl.manage.entity.SysApi;
import cn.csl.manage.service.SysApiServive;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping({"/basics/file"})
public class BasicsFileController {
    @Resource
    private HttpSession httpSession;
    @Resource
    private BasicsFileServive basicsFileServive;
    private String imgSuffix = "jpg,png,gif,jpeg";//允许图片格式
    private String fileSuffix = "";//允许任意文件格式
    private long allowSize = 10000000L;//允许文件大小
    /*
     * 图片存放的基本目录
     */
    private static final String FILE_BASE="/upload";
    private static final String FILE="file";
    private static final String IMG="img";

    public BasicsFileServive getBasicsService() {
        return basicsFileServive;
    }

    private String getBasicsUrl() {
        return "basics/file/";
    }

    /**
     * 单图片上传
     */
    @RequestMapping("/changeSave")
    public void changeSave(@RequestParam("Filedata") MultipartFile file,
                        HttpServletResponse response,
                        BasicsFile basicsFile,
                        HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        ResultResponse resultResponse = originalImage(file,realPath,basicsFile);
        if (resultResponse.isError()){
            resultMap.put("resultCode", "400");
            resultMap.put("resultMsg", resultResponse.getResultMsg());
        }else{
            basicsFile = (BasicsFile)resultResponse.getData();
            resultResponse = basicsFileServive.editChange(basicsFile);
            if (resultResponse.isError()){
                resultMap.put("resultCode", "400");
                resultMap.put("resultMsg", resultResponse.getResultMsg());
            }else{
                basicsFile = (BasicsFile)resultResponse.getData();
                resultMap.put("resultCode", "200");
                resultMap.put("data", basicsFile);
                resultMap.put("resultMsg", "上传成功");
            }
        }
        response.setContentType("application/json; charset=UTF-8");
        Object jsonObject = JSON.toJSON(resultMap);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ResultResponse originalImage(MultipartFile file,String realPath,BasicsFile basicsFile){
        ResultResponse response = new ResultResponse();
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            String allowSuffix = imgSuffix;
            String p = FILE;
            if (basicsFile.getImgFlag()==1){
                p = IMG;
                int length = allowSuffix.indexOf(suffix.toLowerCase());
                if(length == -1){
                    response.setFailureResult("请上传一下格式:"+allowSuffix);
                    return response;
                }
            }

            if(file.getSize() > allowSize){
                response.setFailureResult("您上传的文件大小已经超出范围");
                return response;
            }
            //原始图片路径
            File destFile = new File(realPath+FILE_BASE+"/"+"front"+"/"+p);
//            File destFile = new File(realPath+FILE_BASE+"/"+basicsFile.getSurface()+"/"+basicsFile.getNature());
            if(!destFile.exists()){
                destFile.mkdirs();
            }
            String fileNameNew = RandomUtil.getUUID()+"."+suffix;
            String fileUrl = FILE_BASE+"/"+"front"+"/"+p+"/"+fileNameNew;
//            String fileUrl = FILE_BASE+"/"+basicsFile.getSurface()+"/"+basicsFile.getNature()+"/"+fileNameNew;
            File f = new File(destFile.getAbsoluteFile()+File.separator+fileNameNew);
            file.transferTo(f);
            f.createNewFile();
            basicsFile.setUniName(fileNameNew);//唯一名称
            basicsFile.setRealName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
            basicsFile.setFileType(suffix);
            basicsFile.setFilePath(fileUrl);
            response.setSuccessResult(basicsFile);
            return response;
        } catch (Exception e) {
            response.setFailureResult("失败");
            return response;
        }
    }


    @RequestMapping({"/delete"})
    @ResponseBody
    public ResultResponse delete(String id){
        ResultResponse response = new ResultResponse();
        try{
            response = this.getBasicsService().deleteByPrimaryKey(id);//物理删除
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }
}
