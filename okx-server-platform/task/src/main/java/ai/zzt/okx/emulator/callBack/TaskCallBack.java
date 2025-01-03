package ai.zzt.okx.emulator.callBack;

import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/5/24
 **/
public interface TaskCallBack {

    /**
     * 处理数据
     *
     * @param markPrices 数据
     */
    void run(List<MarkPrice> markPrices);

    /**
     * 任务开始
     */
    void start();

    /**
     * 任务失败
     *
     * @param e 异常
     */
    void fail(Throwable e);

    /**
     * 任务完成
     */
    void finish();

    /**
     * 取消
     */
    void cancel();

    default void setLastProfit(BigDecimal lastProfit){

    }

}
