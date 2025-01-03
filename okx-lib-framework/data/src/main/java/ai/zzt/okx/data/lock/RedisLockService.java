//package ai.zzt.okx.data.lock;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.concurrent.TimeUnit;
//
///**
// *
// */
////@Service // 没解决 RedisTemplate 启动报错问题
//@Slf4j
//public class RedisLockService implements LockService {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    /**
//     * 尝试获取锁的方法
//     *
//     * @param key     锁的键
//     * @param value   锁的值（建议使用唯一标识，比如当前线程 ID）
//     * @param timeout 锁的超时时间（单位：秒）
//     * @return 是否获取到锁
//     */
//    @Override
//    public boolean tryLock(String key, String value, long timeout) {
//        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
//        return success != null && success;
//    }
//
//    /**
//     * 释放锁的方法
//     *
//     * @param key   锁的键
//     * @param value 锁的值（确保只释放自己持有的锁）
//     * @return 是否释放成功
//     */
//    @Override
//    public boolean unlock(String key, String value) {
//        String currentValue = redisTemplate.opsForValue().get(key);
//        if (value.equals(currentValue)) {
//            redisTemplate.delete(key);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 判断锁是否已存在的方法
//     *
//     * @param key 锁的键
//     * @return 是否存在锁
//     */
//    @Override
//    public boolean isLocked(String key) {
//        // 如果 Redis 中存在该 key，表示锁已经被持有
//        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
//    }
//}
