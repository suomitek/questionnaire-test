package cn.csl.basics.util.translate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class CslShanbayTransUtil
{
    private static final String EXAMOLE_SENTENCE_URL = "https://api.shanbay.com/bdc/example/";
    private static final String QUERY_URL = "https://api.shanbay.com/bdc/search/?";

    public static Map<String, Object> query(String query)
    {
        Map<String, Object> reMap = new HashMap();
        Map<String, String> params = new HashMap();
        params.put("word", query);
        String html = HttpGet.get("https://api.shanbay.com/bdc/search/?", params);
        JSONObject dataObject = (JSONObject)JSONObject.parseObject(html).get("data");
        JSONObject phonograms = (JSONObject)dataObject.get("pronunciations");
        if (phonograms != null)
        {
            String phonogram = phonograms.getString("us");
            reMap.put("phonogram", phonogram);
        }
        else
        {
            reMap.put("phonogram", "");
        }
        String id = dataObject.getString("id");
        String definition = dataObject.getString("definition");
        Map<String, String> defnMap = new HashMap();
        if (definition != null)
        {
            String[] defns = definition.split("\n");
            int i = 1;
            for (String defn : defns) {
                if (defn.indexOf(".") == -1)
                {
                    defnMap.put("" + i, defn);
                    i++;
                }
                else
                {
                    String type = defn.substring(0, defn.indexOf(".")).trim();
                    String meaning = defn.substring(defn.indexOf(".") + 1, defn.length()).trim();
                    meaning = meaning.substring(meaning.indexOf(".") + 1, meaning.length()).trim();
                    defnMap.put(type, meaning);
                }
            }
        }
        Map<String, String> params2 = new HashMap();
        params2.put("type", "sys");
        params2.put("vocabulary_id", id);
        html = HttpGet.get("https://api.shanbay.com/bdc/example/", params2);
        try
        {
            JSONArray josnArray = (JSONArray)JSONObject.parseObject(html).get("data");
            if (josnArray.size() > 0)
            {
                dataObject = (JSONObject)josnArray.get(0);
                String translation = dataObject.get("translation").toString();
                String annotation = dataObject.get("annotation").toString().replaceAll("<vocab>", "");
                annotation = annotation.replaceAll("</vocab>", "");
                reMap.put("annotation", annotation);
                reMap.put("translation", translation);
            }
            else
            {
                reMap.put("annotation", "");
                reMap.put("translation", "");
            }
        }
        catch (Exception localException) {}
        reMap.put("defnMap", defnMap);

        return reMap;
    }

    public static void main(String[] args)
    {
        query("like");
    }
}
