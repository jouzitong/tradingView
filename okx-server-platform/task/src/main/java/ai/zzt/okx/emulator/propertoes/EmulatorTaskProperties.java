package ai.zzt.okx.emulator.propertoes;

import lombok.Data;

/**
 * @author zhouzhitong
 * @since 2024/7/26
 **/
@Data
public class EmulatorTaskProperties {

    /**
     * 模拟器同时执行任务数量限制. 默认 100
     */
    private int limit = 100;

    /**
     * 是否开启任务下单信息存储
     */
    private boolean storeOrders = false;

    /**
     * 跑定时任务. 默认 false 不跑任务
     */
    private boolean runScheduledTask = false;

}
