package cn.csl.basics.util.translate;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CssTransUtil
{
    private static final String APP_ID = "20170920000083995";
    private static final String SECURITY_KEY = "OgtzetO_2ANTW3AQaWxI";

    public static Map<String, List<String>> transToEn(String query)
    {
        Map<String, List<String>> reMap = new HashMap();
        try
        {
            TransApi api = new TransApi("20170920000083995", "OgtzetO_2ANTW3AQaWxI");
            String reStr = api.getTransResult(query, "auto", "en");
            JSONObject jsonObject = JSONObject.parseObject(reStr);
            System.out.println(reStr);
            jsonObject = JSONObject.parseObject(jsonObject.get("trans_result").toString().substring(1, jsonObject.get("trans_result").toString().length() - 1));
            String[] dsts = jsonObject.get("dst").toString().split(" ");
            List<String> dstLis = new ArrayList();
            for (int i = 0; i < dsts.length; i++) {
                dstLis.add(dsts[i].trim());
            }
            reMap.put(query, dstLis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return reMap;
    }

    public static Map<String, String> transToZh(String query)
    {
        Map<String, String> reMap = new HashMap();
        try
        {
            String[] contents = query.split(",");
            TransApi api = new TransApi("20170920000083995", "OgtzetO_2ANTW3AQaWxI");
            String reStr = api.getTransResult(query, "auto", "zh");
            JSONObject jsonObject = JSONObject.parseObject(reStr);
            jsonObject = JSONObject.parseObject(jsonObject.get("trans_result").toString().substring(1, jsonObject.get("trans_result").toString().length() - 1));
            String[] dsts = jsonObject.get("dst").toString().split("��");
            for (int i = 0; i < dsts.length; i++)
            {
                reMap.put(contents[i].trim(), dsts[i].trim());
                System.out.println(contents[i].trim() + "=" + dsts[i].trim());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return reMap;
    }

    public static void main(String[] args)
    {
        transToEn("����������������");
        transToZh("Height, length, width");
    }
}
