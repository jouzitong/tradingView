package ai.zzt.okx.calculate.indicator.face.base;

import ai.zzt.okx.calculate.indicator.Indicator;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;

import java.util.List;

/**
 * 计算指标的门面. 主要负责对指标的计算包装, 整合了配置信息
 *
 * @author zhouzhitong
 * @since 2024/7/17
 **/
public interface IndicatorFace<S extends BaseCalculateSettingsFace<?>> {

    /**
     * 设置计算设置
     *
     * @param settingsFace 计算配置
     */
    void setSettingsFace(S settingsFace);

    S getSettingsFace();

    /**
     * 初始化计算器
     *
     * @param mps K线数据
     */
    default void init(Bar bar, List<MarkPrice> mps) {
        mps.forEach(mp -> init(bar, mp));
    }

    /**
     * 初始化计算器
     *
     * @param mp K线数据
     */
    void init(Bar bar, MarkPrice mp);

    /**
     * 添加K线数据
     *
     * @param mp K线数据
     */
    void add(MarkPrice mp);

    /**
     * 分析结果类型
     *
     * @return 分析结果类型
     */
    IndicatorType type();

    /**
     * 获取计算结果
     *
     * @return 计算结果
     */
    <T extends Indicator<?>> T get(Bar bar);

    /**
     * 创建指标实例
     *
     * @param bar 时间周期
     * @return 指标实例
     */
    <T extends Indicator<?>> T createIndex(Bar bar);

    /**
     * TODO 获取计算结果
     *
     * @param s   配置
     * @param bar 时间周期
     * @return
     */
    default <T extends Indicator<?>> T get(S s, Bar bar) {
        return null;
    }

    /**
     * 获取趋势类型
     *
     * @param bar 时间周期
     * @return 趋势类型
     */
    @NotNull
    TrendType getTrend(Bar bar);


    /**
     * TODO 获取局部趋势, 主要用于判断是否达到可以平仓的要求
     *
     * @param bar 时间周期
     * @return 趋势类型
     */
    default TrendType getLocalTrend(Bar bar) {
        return getTrend(bar);
    }


}
