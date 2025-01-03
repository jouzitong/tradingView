package ai.zzt.okx.common.enums;

import ai.zzt.okx.common.base.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 指标类型
 * <ol>
 *     <li>K线: 对数据的连续性进行分析</li>
 *     <li>MACD: 全称“Moving Average Convergence Divergence”，是一种趋势跟踪分析工具，通过计算长短期移动平均线之间的差值，来判断趋势的方向和强度。</li>
 *     <li>RSI: Relative Strength Index，相对强弱指数，是通过比较一段时期内的平均收盘涨跌幅的大小，来分析市场买卖力道的强弱。属于趋势跟踪分析工具。</li>
 *     <li>BOLL: 布林线，是一种统计学分析工具，用于判断股价是否已超出常态分布范围。属于趋势跟踪分析工具。</li>
 *     <li>巨幅差值分析. 如果是一天变化幅度太大, 则下单时应该着重考虑短期和回测率问题</li>
 * </ol>
 *
 * @author zhouzhitong
 * @since 2024/5/5
 **/
@Getter
public enum IndicatorType implements IEnum {

    K_LINE(1, "K line"),   // K线分析
    MACD(2, "MACD"),   // MACD
    RSI(3, "RSI"),     // RSI
    BOLL(4, "BOLL"),     // BOLL
    KDJ(5, "KDJ"),     // BOLL
    ;

    private final int code;

    @JsonValue
    private final String name;

    IndicatorType(int code, String name) {
        this.code = code;
        this.name = name;
    }


}
