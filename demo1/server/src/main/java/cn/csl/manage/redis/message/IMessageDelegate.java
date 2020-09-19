package cn.csl.manage.redis.message;

import java.io.Serializable;
import java.util.Map;

public interface IMessageDelegate {
    void handleMessage(String message);
    void handleMessage(Map message);
    void handleMessage(byte[] message);
    void handleMessage(Serializable message);
    void handleMessage(Serializable message, String channel);
}