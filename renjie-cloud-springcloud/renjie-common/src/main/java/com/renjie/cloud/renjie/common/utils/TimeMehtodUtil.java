package com.renjie.cloud.renjie.common.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.*;

/**
 * @Author oyg
 * @Date 2018/10/11/19:45
 */
@Component
public class TimeMehtodUtil {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static JedisPool pool;


    static {
        initPool();
    }


    private static int maxTotal = 20;

    private static int maxIdle = 10;

    private static int minIdle = 5;

    private static boolean testOnBorrow = true;

    private static boolean testOnReturn = false;

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config, "127.0.0.1",6379,5000);
    }
    public static Jedis getJedis(){
        return pool.getResource();
    }
}
