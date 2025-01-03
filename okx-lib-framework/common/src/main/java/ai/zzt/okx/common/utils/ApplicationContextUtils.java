package ai.zzt.okx.common.utils;

import ai.zzt.okx.common.context.SystemContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 禁止现在项目启动期间, 调用此方法.
 * <p>
 * 原因: 是有可能其他的组件还没初始化完成, 导致出现未知的错误.
 *
 * @author zhouzhitong
 * @since 2024/5/19
 **/
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz, false);
    }

    /**
     * 获取 IOC Bean 对象
     *
     * @param clazz Bean对象类型
     * @param force 是否强制查询
     * @param <T>   Bean对象类型
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz, boolean force) {
        if (!(SystemContext.isRunning() || force)) {
            throw new IllegalStateException("系统正在启动, 禁止调用此方法.");
        }
        return applicationContext.getBean(clazz);
    }

}
