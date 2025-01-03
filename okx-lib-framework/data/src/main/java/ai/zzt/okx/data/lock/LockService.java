package ai.zzt.okx.data.lock;

/**
 * 加锁服务接口
 *
 * @author zhouzhitong
 * @since 2024/10/20
 **/
public interface LockService {

    /**
     * 尝试获取锁的方法
     *
     * @param key     锁的键
     * @param value   锁的值（建议使用唯一标识，比如当前线程 ID）
     * @param timeout 锁的超时时间（单位：秒）
     * @return 是否获取到锁
     */
    boolean tryLock(String key, String value, long timeout);

    /**
     * 释放锁的方法
     *
     * @param key   锁的键
     * @param value 锁的值（确保只释放自己持有的锁）
     * @return 是否释放成功
     */
    boolean unlock(String key, String value);

    /**
     * 判断锁是否已存在的方法
     *
     * @param key 锁的键
     * @return 是否存在锁
     */
    boolean isLocked(String key);

}
