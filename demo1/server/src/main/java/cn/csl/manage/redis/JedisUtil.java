package cn.csl.manage.redis;

import cn.csl.manage.redis.JedisPoolWriper;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.csl.manage.redis.utils.*;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

public class JedisUtil {

    /**
     * 缓存生存时间
     */
    private final int expire = 60000;
    /**
     * 操作Key的方法
     */
    public Keys KEYS;
    /**
     * 对存储结构为String类型的操作
     */
    public Strings STRINGS;
    /**
     * 对存储结构为List类型的操作
     */
    public Lists LISTS;
    /**
     * 对存储结构为Set类型的操作
     */
    public Sets SETS;
    /**
     * 对存储结构为HashMap类型的操作
     */
    public Hash HASH;

    /**
     * Redis连接池对象
     */
    private JedisPool jedisPool;

    /**
     * 获取redis连接池
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 设置redis连接池
     */
    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    /**
     * 从jedis连接池中获取获取jedis对象
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }


    /**
     * 设置过期时间
     *
     * @author xiangze
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        jedis.close();
    }

    /**
     * 设置默认过期时间
     *
     * @author xiangze
     */
    public void expire(String key) {
        expire(key, expire);
    }

    public int getExpire() {
        return expire;
    }

    public Keys getKEYS() {
        return KEYS;
    }

    public void setKEYS(Keys KEYS) {
        this.KEYS = KEYS;
    }

    public Strings getSTRINGS() {
        return STRINGS;
    }

    public void setSTRINGS(Strings STRINGS) {
        this.STRINGS = STRINGS;
    }

    public Lists getLISTS() {
        return LISTS;
    }

    public void setLISTS(Lists LISTS) {
        this.LISTS = LISTS;
    }

    public Sets getSETS() {
        return SETS;
    }

    public void setSETS(Sets SETS) {
        this.SETS = SETS;
    }

    public Hash getHASH() {
        return HASH;
    }

    public void setHASH(Hash HASH) {
        this.HASH = HASH;
    }
}
