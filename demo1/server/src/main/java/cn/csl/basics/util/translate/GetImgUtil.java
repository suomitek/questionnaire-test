package cn.csl.basics.util.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetImgUtil
{
    private static String URL = "http://image.baidu.com/search/avatarjson";

    public static String getImgBySizeAndSearch(float targetWidth, float targetHeight, int page, String word)
    {
        Map<String, String> params = new HashMap();
        params.put("tn", "resultjsonavatarnew");
        params.put("ie", "utf-8");
        params.put("word", word);
        params.put("rn", "50");
        params.put("pn", page + "");
        params.put("cg", "girl");
        String html = HttpGet.get(URL, params);
        JSONObject dataObject = JSONObject.parseObject(html);
        JSONArray josnArray = (JSONArray)dataObject.get("imgs");
        JSONObject imgObject = new JSONObject();
        float proportion = targetWidth / targetHeight;float thisProportion = 0.0F;
        String objURL = null;
        for (Object object : josnArray)
        {
            imgObject = (JSONObject)object;
            float width = Float.parseFloat(imgObject.get("width").toString());
            float height = Float.parseFloat(imgObject.get("height").toString());
            if (objURL == null)
            {
                objURL = imgObject.get("objURL").toString();
                if ((objURL.length() != objURL.getBytes().length) || (fangDaoLian(objURL))) {
                    objURL = null;
                } else {
                    thisProportion = width / height;
                }
            }
            else if (near(proportion, thisProportion, width / height))
            {
                objURL = imgObject.get("objURL").toString();
                if ((objURL.length() != objURL.getBytes().length) || (fangDaoLian(objURL))) {
                    objURL = null;
                } else {
                    thisProportion = width / height;
                }
            }
        }
        return objURL;
    }

    private static boolean fangDaoLian(String url)
    {
        List<String> delMap = new ArrayList();
        delMap.add("baidu");
        delMap.add("nipic");
        delMap.add("sina");
        int num = 0;
        for (int i = 0; i < delMap.size(); i++) {
            if (url.indexOf((String)delMap.get(i)) == -1) {
                num++;
            }
        }
        if (num == delMap.size()) {
            return false;
        }
        return true;
    }

    private static boolean near(float proportion0, float proportion1, float proportion2)
    {
        if (Math.abs(proportion0 - proportion1) < Math.abs(proportion0 - proportion2)) {
            return false;
        }
        return true;
    }
}
