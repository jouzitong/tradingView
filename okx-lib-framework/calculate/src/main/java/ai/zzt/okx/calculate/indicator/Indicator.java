package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 计算指标基类
 *
 * @author zhouzhitong
 * @since 2024/6/19
 **/
public interface Indicator<T> extends Serializable {

    /**
     * 初始化
     *
     * @param mps MarkPrice列表
     */
    default void init(List<MarkPrice> mps) {
        mps.forEach(this::init);
    }

    /**
     * 初始化
     *
     * @param mp MarkPrice列表
     */
    void init(MarkPrice mp);

    /**
     * 添加价格
     *
     * @param price 价格
     * @param time  时间
     * @return 是否计算
     */
    T add(BigDecimal price, long time);

    /**
     * 获取计算结果
     *
     * @return 计算结果
     */
    T get();

    /**
     * 获取当前价格
     *
     * @return 当前价格
     */
    BigDecimal getPrice();

}
