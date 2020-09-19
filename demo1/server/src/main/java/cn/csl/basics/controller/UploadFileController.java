package cn.csl.basics.controller;

import cn.csl.basics.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/basics/uploadFile")
public class UploadFileController {
    private String FILE_BASE = "/upload";
    private String fileUpload(MultipartFile file,String path,String typeName,String modelName, String allowSuffix, long allowSize)throws IOException{
//如果文件不为空，写入上传路径
        if(!file.isEmpty()) {
            //上传文件名
//            String filename = file.getOriginalFilename();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            String fileNameNew = RandomUtil.getCurrentDateSequen()+"."+suffix;
            String fileUrl = FILE_BASE+"/"+typeName+"/"+modelName+"/"+fileNameNew;
            int length = allowSuffix.indexOf(suffix.toLowerCase());
            if(length == -1){
                return getError("支持上传文件类型:"+allowSuffix);
            }
            if(file.getSize() > allowSize){
                return getError("您上传的文件大小已经超出范围:");
            }

            File filepath = new File(path + File.separator +fileUrl);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator +fileUrl));
            return getSuccess(fileUrl);
        } else {
            return getError("支持上传文件类型:"+allowSuffix);
        }
    }
    @RequestMapping(value="/excel",method=RequestMethod.POST)
    public void excel(HttpServletRequest request,HttpServletResponse response,
                         @RequestParam("modelName") String modelName,
                         @RequestParam("Filedata") MultipartFile file) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String allowSuffix = "xls,xlsx";
        long allowSize = 10000000L;
        String resultMap = fileUpload(file,realPath,"excel",modelName,allowSuffix,allowSize);
        response.setContentType("application/json; charset=UTF-8");
        Object jsonObject = JSON.toJSON(resultMap);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getError(String message) {
        JSONObject obj = new JSONObject();
        obj.put("resultcode", "400");
        obj.put("resultmsg", message);
        return obj.toString();
    }
    private String getSuccess(String message) {
        JSONObject obj = new JSONObject();
        obj.put("resultcode", "200");
        obj.put("resultmsg", message);
        return obj.toString();
    }
}
