package com.renjie.cloud.renjie.common.lock;

import com.renjie.cloud.renjie.common.utils.RedisTool;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @Author oyg
 * @Date 2018/10/13/12:00
 */
@Component
@Scope(value = "prototype")
public class RedisLockDelay extends RedisLock{

    @Override
    public void lock() {
        while (true){
            boolean result = RedisTool.tryGetDistributedLock(lockKey, lockRequestId, 30);
            if (result){
                System.err.println("线程id:"+threadNum+ "加锁成功!时间:"+LocalTime.now());
                isOpenExpirationRenewal = true;
                scheduleExpirationRenewal();
                break;
            }
            System.err.println("线程id:"+threadNum + "获取锁失败，休眠10秒!时间:"+LocalTime.now());
            sleepBySecond(10);
        }
    }

    @Override
    public void unlock() {
        System.err.println("线程id:"+Thread.currentThread().getId() + "解锁!时间:"+LocalTime.now());
        RedisTool.releaseDistributedLock(lockKey, lockRequestId);
        isOpenExpirationRenewal = false;
    }

    @Override
    protected void renewalTime() {
        System.err.println("线程id:"+threadNum+"加长延迟时间");
        String checkAndExpireScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('expire',KEYS[1],ARGV[2]) " +
                "else " +
                "return 0 end";
        RedisTool.expirationRenewal(checkAndExpireScript, 1, lockKey, lockRequestId, "30");

        //休眠10秒
        sleepBySecond(10);
    }
}
