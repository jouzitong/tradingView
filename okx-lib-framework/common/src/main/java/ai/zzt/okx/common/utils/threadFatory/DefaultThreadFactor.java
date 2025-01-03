package ai.zzt.okx.common.utils.threadFatory;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Slf4j
public class DefaultThreadFactor implements ThreadFactory {

    /**
     * thread pool name
     */
    private final String poolName;

    private long count = 0;

    public DefaultThreadFactor(String poolName) {
        this.poolName = poolName;

    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        count++;
        log.debug("线程池: {} 创建了线程{}", poolName, count);
        return new ThreadWrapper(r, genThreadName());
    }

    private String genThreadName() {
        return STR."\{poolName}-\{count}";
    }

}
