package ai.zzt.okx.settings.order;

import ai.zzt.okx.common.base.settings.BaseSettings;
import ai.zzt.okx.settings.order.profit.ProfitSettings;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 下单配置
 *
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PlaceOrderSettings extends BaseSettings {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * 是否允许开仓. 默认不允许
     */
    private boolean enablePlaceOrder = false;

    /**
     * 下单金额. 单位: USDT(约等于美元) .默认 100
     * <p>
     * 描述: 下单金额. 实际等价金额=cash * level（杠杆）
     */
    private BigDecimal cash = BigDecimal.valueOf(100);

    /**
     * TODO 开仓模式. 默认是 ISOLATED(逐仓模式)
     */
    private TdMode tdMode = TdMode.ISOLATED;

    /**
     * 开仓订单类型
     */
    private OrderType openOrderType = OrderType.MARKET;

    /**
     * 平仓订单类型
     */
    private OrderType closeOrderType = OrderType.MARKET;

    /**
     * TODO 回撤率(百分比)
     * <p>
     * 描述: 这个值是由于计算分析出来的结果可能不是最佳的, 所以需要设置一个回撤率, 保证不会亏损太多
     */
    private BigDecimal retraceRate = BigDecimal.valueOf(0.01);

    /**
     * 杠杆倍数. 默认是1倍
     */
    private BigDecimal lever = BigDecimal.valueOf(1);

    /**
     * 下单限制
     */
    private PlaceOrderLimit limit = new PlaceOrderLimit();

    /**
     * 利润配置
     */
    private ProfitSettings profit = new ProfitSettings();

    /**
     * 止损率(包括杠杠倍数), 如果值大于0, 不进行止损. 默认是 -20%
     */
    private BigDecimal stopLossRate = BigDecimal.valueOf(-0.1);

    /**
     * 持仓最长时间, 单位: 毫秒. 默认 7天
     */
    private long positionMaxTime = 1000L * 60 * 60 * 24 * 7;

    public PlaceOrderSettings(String id) {
        super(id);
    }

    public boolean isSupportStopLoss() {
        return stopLossRate.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isSupportMaxTime() {
        return positionMaxTime <= 0;
    }

    @Setter
    @Getter
    @ToString
    public static class PlaceOrderLimit {

        /**
         * 下单最大数量. 默认 1
         */
        private int maxCount = 1;

        /**
         * 多次开仓时, 最少间隔时间, 单位 毫秒(ms). 默认 4小时 = 14400000 ms
         */
        private long minIntervalTime = 1000L * 60 * 60 * 4;

        /**
         * 支持的币种类型
         * <p>
         * 描述: 开仓按照币种类型顺序开仓(只开仓一次). 例如 要开多仓, 币币支持多仓, 所以开币币类型. 要是开空仓, 币币不支持空仓, 就只能顺延到 杠杠, 合约等, 直到支持开空仓为止.
         */
        private List<InstrumentType> supportTypes = List.of(InstrumentType.SPOT, InstrumentType.MARGIN, InstrumentType.SWAP);

        /**
         * 支持的购买方向. 默认 买空、买多. 只有在 SWAP 和 MARGIN 模式下, 才能买空
         */
        private Set<PositionsSide> sides = Set.of(PositionsSide.LONG, PositionsSide.SHORT);

        /**
         * 下单冷静期. 单位: 毫秒(ms). 默认 无冷静期
         * <p>
         * 描述: 就是在系统认定趋势后, 支持持续了这么久, 然后再下单, 防止系统误判.
         */
        private long coolingOffTime = 0L;

        /**
         * 开仓时间间隔。 单位: 毫秒(ms). 默认 1天
         * <p>
         * 描述: 上一次开仓时间间隔，单位: 毫秒(ms), 默认 1天
         */
        private long openIntervalTime = 24 * 60 * 60 * 1000L;

        /**
         * 开多仓最高价
         */
        private BigDecimal maxPrice;

        /**
         * 开空仓最低价
         */
        private BigDecimal minPrice;

    }

}
