package cn.csl.basics.controller;

import cn.csl.basics.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skyjilygao.util.VideoConvert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 视频处理
 * @author skyjilygao
 * @since 20181129
 * @version 1.0.0
 */
@Controller
@RequestMapping("/basics/admin/video")
public class VideoController {
    private String allowSuffix = "mp4,MP4,avi,rmvb";//允许文件格式

    private String source;
    private String target;
    private String ffmpeg;

    public String getAllowSuffix() {
        return allowSuffix;
    }

    public void setAllowSuffix(String allowSuffix) {
        this.allowSuffix = allowSuffix;
    }

    @RequestMapping(value="/upload",method=RequestMethod.POST)
    public void upload(@RequestParam("Filedata")MultipartFile file, HttpServletResponse response, String modelName, HttpServletRequest request) throws IOException{
        Map<String, String> resultMap = new HashMap<String, String>();
        String path =  request.getSession().getServletContext().getRealPath("/") + "upload" + File.separatorChar
                + "video" + File.separatorChar +modelName+ File.separatorChar;
        String fileName = file.getOriginalFilename();
        String newName = RandomUtil.getCurrentDateSequen();
        File dir = new File(path+newName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        response.setContentType("application/json; charset=UTF-8");
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        int length = getAllowSuffix().indexOf(suffix);
        try {
            if(length == -1){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "请上传允许格式的文件");
            }else {
                String fileNameNew = newName+"."+suffix;
                File f = new File(dir+File.separator+fileNameNew);
                file.transferTo(f);
                f.createNewFile();
                String source = dir+File.separator+fileNameNew;
                String target = dir+File.separator+newName+".m3u8";//"E:\\test\\video2.m3u8";
                String ffmpegPath = "D:\\huanjing\\ffmpeg\\bin\\ffmpeg.exe";
                VideoConvert convert = new VideoConvert(ffmpegPath);
                convert.start(source, target);
                resultMap.put("resultcode", "200");
                resultMap.put("videoUrl", "/upload/video/"+modelName+"/"+newName+"/"+newName+".m3u8");
                resultMap.put("resultmsg", "上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultcode", "400");
            resultMap.put("resultmsg", "请上传允许格式的文件");
        }
        Object jsonObject = JSON.toJSON(resultMap);
        response.getWriter().print(jsonObject);
    }
}
