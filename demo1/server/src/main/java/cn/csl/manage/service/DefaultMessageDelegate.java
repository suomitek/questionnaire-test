package cn.csl.manage.service;

import cn.csl.manage.redis.JedisUtil;
import cn.csl.manage.redis.message.IMessageDelegate;
import cn.csl.wx.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.Map;

@Service
public class DefaultMessageDelegate implements IMessageDelegate {
    @Autowired
    private JedisUtil jedisUtil;
    @Override
    public void handleMessage(String message) {
        if (String.valueOf(message).startsWith("Timer_")){//以这个开头的都是 定时器
            timer(String.valueOf(message));
        }
        System.out.println(message);
    }

    @Override
    public void handleMessage(Map message) {
        if (String.valueOf(message).startsWith("Timer_")){//以这个开头的都是 定时器
            timer(String.valueOf(message));
        }
        System.out.println(message);
    }

    @Override
    public void handleMessage(byte[] message) {
        if (String.valueOf(message).startsWith("Timer_")){//以这个开头的都是 定时器
            timer(String.valueOf(message));
        }
        System.out.println(message);
    }

    @Override
    public void handleMessage(Serializable message) {
        if (String.valueOf(message).startsWith("Timer_")){//以这个开头的都是 定时器
            timer(String.valueOf(message));
        }
        System.out.println(message);
    }

    @Override
    public void handleMessage(Serializable message, String channel) {
        if (String.valueOf(message).startsWith("Timer_")){//以这个开头的都是 定时器
            timer(String.valueOf(message));
        }
//        JSONObject resultObject = HttpUtil.httpsRequest(url, "POST", null);
        System.out.println(message);
    }
    public void timer(String key){
        String url = jedisUtil.STRINGS.get(String.valueOf(key)+"_url");
        jedisUtil.KEYS.del(String.valueOf(key)+"_url");//用完删除
        HttpUtil.httpRequest(url, "POST", null);
        System.out.println(url);
    }
}