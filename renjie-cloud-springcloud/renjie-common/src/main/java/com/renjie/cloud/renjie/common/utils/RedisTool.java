package com.renjie.cloud.renjie.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @Author oyg
 * @Date 2018/10/1/11:53
 */
@Component
public class RedisTool {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    private static Jedis jedis;
    @Autowired
    private Jedis jedisIns;

    @PostConstruct
    public void init(){
        this.jedis = jedisIns;
    }

    public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime){
        return tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
    }

    public static boolean releaseDistributedLock(String lockKey, String requestId){
        return releaseDistributedLock(jedis, lockKey, requestId);
    }

    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime){

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }

    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)){
            return true;
        }
        return false;
    }

    public static void expirationRenewal(String checkAndExpireScript, int keyCount, String lockKey, String requestId, String expireTime){
        jedis.eval(checkAndExpireScript, keyCount, lockKey, requestId, expireTime);
    }

}
