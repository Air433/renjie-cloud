package com.renjie.cloud.renjie.common.lock;

/**
 * @Author oyg
 * @Date 2018/10/13/11:59
 */
public abstract class RedisLock {

    protected String lockKey;

    protected String lockRequestId;

    protected long threadNum = Thread.currentThread().getId();

    protected volatile boolean isOpenExpirationRenewal = true;

    public abstract void lock();

    public abstract void unlock();

    protected abstract void renewalTime();

    protected void scheduleExpirationRenewal(){
        new Thread(new ExpirationRenewal()).start();
    }

    private class ExpirationRenewal implements Runnable{

        @Override
        public void run() {
            while (isOpenExpirationRenewal){
//                System.err.println("线程id:"+threadNum+"加长延迟时间");
//                String checkAndExpireScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
//                        "return redis.call('expire',KEYS[1],ARGV[2]) " +
//                        "else " +
//                        "return 0 end";
//                RedisTool.expirationRenewal(checkAndExpireScript, 1, lockKey, lockRequestId, "30");
//
//                //休眠10秒
//                sleepBySecond(10);
                renewalTime();
            }
        }
    }

    public void sleepBySecond(int second){

        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getLockRequestId() {
        return lockRequestId;
    }

    public void setLockRequestId(String lockRequestId) {
        this.lockRequestId = lockRequestId;
    }
}
