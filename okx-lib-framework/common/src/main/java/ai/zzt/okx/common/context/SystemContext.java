package ai.zzt.okx.common.context;

import ai.zzt.okx.common.enums.SystemName;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统上下文
 *
 * @author zhouzhitong
 * @since 2024/5/1
 **/
@Slf4j
public class SystemContext {

    /**
     * TODO 系统版本号（不确定是否应该放在这）
     */
    @Getter
    @Setter
    private static String version = "v0.3.1";

    /**
     * 系统名
     */
    @Setter
    @Getter
    private static SystemName systemName;

    /**
     * 获取系统状态
     */
    @Getter
    private static volatile boolean running = false;

    /**
     * 关闭理由
     */
    @Getter
    private static volatile String msg = "系统还没启动完成";

    public static void open() {
        log.info("系统上下文开启");
        running = true;
    }

    @Deprecated
    public static void close() {
        log.info("系统上下文关闭");
        running = false;
        // 并打断当前线程
        Thread.interrupted();
    }

    /**
     * 关闭系统上下文
     *
     * @param msg 关闭理由
     */
    public static void close(String msg) {
        log.info("系统上下文关闭");
        SystemContext.running = false;
        SystemContext.msg = msg;
        // 并打断当前线程
        Thread.interrupted();
    }

    /**
     * 判断是否是量化交易系统
     *
     * @return
     */
    public static boolean isTradeView() {
        return SystemName.TRADE_VIEW == systemName;
    }

    /**
     * 获取系统当前时间(相对的)
     * <p>
     * TODO 应该有个线程安全的类来管理系统上下文, 当前的时间由自定义接口实现. 尤其是模拟器功能需要
     *
     * @return 系统当前时间(相对的)
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long minTimeMillis() {
        // 最小时间2019年1月1号
        return 1546300800000L;
    }

}
