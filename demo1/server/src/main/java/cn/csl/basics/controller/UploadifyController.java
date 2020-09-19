package cn.csl.basics.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.csl.basics.util.RandomUtil;

import com.alibaba.fastjson.JSON;

/**
 * 针对uploadify图片上传
 * @introduction
 * @author rookie
 * 创建日期：2016年1月7日
 */
@Controller
@RequestMapping("/basics/admin/uploadify")
public class UploadifyController {
    //private static final Logger LOGGER = LoggerFactory.getLogger(UploadifyController.class);

    private String allowSuffix = "jpg,png,gif,jpeg";//允许文件格式
    private long allowSize = 10000000L;//允许文件大小

    public String getAllowSuffix() {
        return allowSuffix;
    }

    public void setAllowSuffix(String allowSuffix) {
        this.allowSuffix = allowSuffix;
    }

    public long getAllowSize() {
        return allowSize;
    }

    public void setAllowSize(long allowSize) {
        this.allowSize = allowSize;
    }

    /*
     * 图片存放的基本目录
     */
    private static final String IMAGE_BASE="/upload";

    /**
     * 多文件上传
     * 创建日期：2016年4月21日
     * @author rookie
     * explain：
     */
    @RequestMapping("/multiupload")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("file")MultipartFile[] files,String modelName,String entityName,HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if(files==null||files.length<=0){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "请选择图片");
                return resultMap;
            }
            List<Map<String, String>> imageUrls = uploadimages(files,request,modelName,entityName);
            if(imageUrls==null||imageUrls.size()<=0||files.length!=imageUrls.size()){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "上传图片失败");
            }else{
                resultMap.put("resultcode", "200");
                resultMap.put("imageUrls", imageUrls);
                resultMap.put("resultmsg", "上传成功");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultMap.put("resultcode", "400");
            resultMap.put("resultmsg", "上传失败");
        }
        return resultMap;

    }

    /**
     * 单图片上传（生成原图、中、小三种图片）
     * @param file
     * @param modelName
     * @param request
     * @return
     */
    @RequestMapping("/image/single")
    @ResponseBody
    public Map<String, String> uploadUrl(@RequestParam("Filedata")MultipartFile file,String modelName,String entityName,HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();
        String path = request.getContextPath();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        try {
            Map<String, String> imageUrls = uploadimage(file,path,realPath,modelName,entityName);
            if(imageUrls==null){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "上传图片失败");
            }else{
                resultMap.put("resultcode", "200");
                resultMap.put("pImageUrl", imageUrls.get("pImageUrl"));
                resultMap.put("mImageUrl", imageUrls.get("mImageUrl"));
                resultMap.put("resultmsg", "上传成功");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultMap.put("resultcode", "400");
            resultMap.put("resultmsg", "上传失败");
        }
        return resultMap;

    }



    /**
     * 图片上传处理（生成原图、中、小三种图片）
     * 创建日期：2016年1月7日
     * @author rookie
     * @param modelName   图片所属模块，将这个名称当作存放目录的一部分
     * @param path
     * @param realPath
     * @throws Exception
     */
    private Map<String, String> uploadimage(MultipartFile file,String path,String realPath,String modelName,String entityName) throws Exception{
        Map<String, String> urlMaps = new HashMap<String, String>();
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            int length = getAllowSuffix().indexOf(suffix);
            if(length == -1){
                throw new Exception("请上传允许格式的文件");
            }
            if(file.getSize() > getAllowSize()){
                throw new Exception("您上传的文件大小已经超出范围");
            }
            //原始图片路径
            File destFile = new File(realPath+IMAGE_BASE+"/"+modelName+"/"+entityName);
            if(!destFile.exists()){
                destFile.mkdirs();
            }
            String fileNameNew = RandomUtil.getCurrentDateSequen()+"."+suffix;
            String baseImagePath = IMAGE_BASE+File.separator+modelName;
            String imageUrl = IMAGE_BASE+File.separator+modelName+File.separator+fileNameNew;
            File f = new File(destFile.getAbsoluteFile()+File.separator+fileNameNew);
            file.transferTo(f);
            f.createNewFile();
            //中图标地址
            String mUrl = thumbnailImage(f,100,100,"m",false,baseImagePath);
            //小图标地址
            String sUrl = thumbnailImage(f,100,100,"s",false,baseImagePath);
            urlMaps.put("bImageUrl", imageUrl);
            urlMaps.put("mImageUrl", mUrl);
            urlMaps.put("sImageUrl", sUrl);
            return urlMaps;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 单图片上传
     */
    @RequestMapping("/image/originalSingle")
//    public ResultResponse originalSingle(Model model, HttpServletResponse response, String modelName, HttpServletRequest request){
    public void originalSingle(@RequestParam("Filedata")MultipartFile file, HttpServletResponse response, String modelName, HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String imageUrl = originalImage(file,realPath,modelName);
            if(StringUtils.isBlank(imageUrl)){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "上传图片失败");
            }else{
                resultMap.put("resultcode", "200");
                resultMap.put("imageUrl", imageUrl);
                resultMap.put("resultmsg", "上传成功");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultMap.put("resultcode", "400");
            resultMap.put("resultmsg", "上传失败");
        }
        response.setContentType("application/json; charset=UTF-8");
        //net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(resultMap);
        Object jsonObject = JSON.toJSON(resultMap);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 图片上传处理（不生成缩略图片）
     * 创建日期：2016年1月7日
     * @author rookie
     * @param modelName   图片所属模块，将这个名称当作存放目录的一部分
     * @param realPath
     * @throws Exception
     */
    private String originalImage(MultipartFile file,String realPath,String modelName) throws Exception{
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            int length = getAllowSuffix().indexOf(suffix.toLowerCase());
            if(length == -1){
                throw new Exception("请上传允许格式的文件");
            }
            if(file.getSize() > getAllowSize()){
                throw new Exception("您上传的文件大小已经超出范围");
            }
            //原始图片路径
            File destFile = new File(realPath+IMAGE_BASE+"/"+modelName);
            if(!destFile.exists()){
                destFile.mkdirs();
            }
            String fileNameNew = RandomUtil.getCurrentDateSequen()+"."+suffix;
            String imageUrl = IMAGE_BASE+"/"+modelName+"/"+fileNameNew;
            File f = new File(destFile.getAbsoluteFile()+File.separator+fileNameNew);
            file.transferTo(f);
            f.createNewFile();
            return imageUrl;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public String thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force,String imgPath){
        if(imgFile.exists()){
            try {
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }
                Image img = ImageIO.read(imgFile);
                if(!force){
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                String originalURl = p.substring(0,p.lastIndexOf(File.separator));
                //新建文件
                File destFile = new File(originalURl);
                if(!destFile.exists()){
                    destFile.mkdirs();
                }
                String thumUrl = originalURl+File.separator+prevfix+imgFile.getName();
                // 将图片保存在原目录并加上前缀
                File newFile = new File(thumUrl);
                if(!newFile.exists()){
                    newFile.createNewFile();
                }
                ImageIO.write(bi, suffix, newFile);
                //图片相对存储路径
                String relativeImageUrl = imgPath+File.separator+prevfix+imgFile.getName();
                return relativeImageUrl;
            } catch (IOException e) {
                //LOGGER.error("上传图片生成缩略图时出现异常",e);
                return "";
            }
        }else{
            return "";
        }
    }

    /**
     * 多文件处理逻辑
     * 创建日期：2016年1月7日
     * @author rookie
     * explain：
     */
    private List<Map<String, String>> uploadimages(MultipartFile[] files,HttpServletRequest request,String modelName,String entityName) throws Exception{
        String path = request.getContextPath();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        List<Map<String, String>> imageUrls = new ArrayList<Map<String, String>>();
        int size = files.length;
        for(int i=0;i<size;i++){
            Map<String, String> imageUrlMap = uploadimage(files[i],path,realPath,modelName,entityName);
            if(imageUrlMap!=null){
                imageUrls.add(imageUrlMap);
            }
        }
        return imageUrls;
    }

    /**
     * 图片上传4：3(新闻、产品图册)
     * @param file
     * @param modelName
     * @param request
     * @return
     */
    @RequestMapping("/image/np")
    @ResponseBody
    public Map<String, String> newsImageSingle(@RequestParam("Filedata")MultipartFile file,String modelName,HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();
        String path = request.getContextPath();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        try {
            Map<String, String> imageUrls = newsImage(file,path,realPath,modelName);
            if(imageUrls==null){
                resultMap.put("resultcode", "400");
                resultMap.put("resultmsg", "上传图片失败");
            }else{
                resultMap.put("resultcode", "200");
                resultMap.put("bImageUrl", imageUrls.get("bImageUrl"));
                resultMap.put("mImageUrl", imageUrls.get("mImageUrl"));
                resultMap.put("sImageUrl", imageUrls.get("sImageUrl"));
                resultMap.put("resultmsg", "上传成功");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultMap.put("resultcode", "400");
            resultMap.put("resultmsg", "上传失败");
        }
        return resultMap;

    }
    /**
     * 图片上传处理（生成原图、中、小三种图片）
     * 创建日期：2016年1月7日
     * @author rookie
     * @param modelName   图片所属模块，将这个名称当作存放目录的一部分
     * @param path
     * @param realPath
     * @throws Exception
     */
    private Map<String, String> newsImage(MultipartFile file,String path,String realPath,String modelName) throws Exception{
        Map<String, String> urlMaps = new HashMap<String, String>();
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            int length = getAllowSuffix().indexOf(suffix);
            if(length == -1){
                throw new Exception("请上传允许格式的文件");
            }
            if(file.getSize() > getAllowSize()){
                throw new Exception("您上传的文件大小已经超出范围");
            }
            //原始图片路径
            File destFile = new File(realPath+IMAGE_BASE+"/"+modelName);
            if(!destFile.exists()){
                destFile.mkdirs();
            }
            String fileNameNew = RandomUtil.getCurrentDateSequen()+"."+suffix;
            String baseImagePath = IMAGE_BASE+File.separator+modelName;
            String imageUrl = IMAGE_BASE+File.separator+modelName+File.separator+fileNameNew;
            File f = new File(destFile.getAbsoluteFile()+File.separator+fileNameNew);
            file.transferTo(f);
            f.createNewFile();
            //中图标地址
            String mUrl = thumbnailImage(f,200,150,"m",false,baseImagePath);
            //小图标地址
            String sUrl = thumbnailImage(f,100,75,"s",false,baseImagePath);
            urlMaps.put("bImageUrl", imageUrl);
            urlMaps.put("mImageUrl", mUrl);
            urlMaps.put("sImageUrl", sUrl);
            return urlMaps;
        } catch (Exception e) {
            return null;
        }
    }

}
