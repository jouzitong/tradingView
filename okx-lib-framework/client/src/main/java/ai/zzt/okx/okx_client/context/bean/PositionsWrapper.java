package ai.zzt.okx.okx_client.context.bean;

import ai.zzt.okx.okx_client.utils.ProfitUtils;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/7/28
 **/
@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PositionsWrapper {

    /**
     * 仓位信息
     * <p>
     * TODO 这个属性应该是私有的, 外部不应该直接访问
     */
    public Positions positions;

    /**
     * 当前价格
     */
    public BigDecimal px;

    /**
     * 当前利润率【手续费不在计算范围内】
     */
    public BigDecimal profitRate = null;

    /**
     * 回撤金额
     * <p>
     * TODO 此时记录的是回撤金额, 但更应该记录回撤率
     */
    public ProfitVO retrace = new ProfitVO();

    /**
     * 最大利润
     */
    public ProfitVO maxProfit = new ProfitVO();

    /**
     * 首次最大利润
     */
    public ProfitVO firstMaxProfit = new ProfitVO();

    /**
     * 条件平仓收益率
     */
    public BigDecimal conditionalCloseProfitRate = BigDecimal.ZERO;

    /**
     * 是否已产生首次正利润
     */
    private boolean hasPositiveProfit = true;

    /**
     * 绑定的订单【TODO 是否应该考虑一个集合】
     */
    public List<Object> orders = new ArrayList<>();


    /**
     * 开仓次数
     */
    public int openCount = 1;

    public PositionsWrapper(Positions positions) {
        this.positions = positions;
        clear();
    }

    public void clear() {
        positions = null;
        profitRate = null;
        retrace = new ProfitVO();
        maxProfit = new ProfitVO();
        firstMaxProfit = new ProfitVO();
        hasPositiveProfit = true;
        openCount = 0;
        conditionalCloseProfitRate = BigDecimal.ZERO;
        orders.clear();
    }

    /**
     * 更新回撤金额和最大利润金额信息
     *
     * @param px 当前价格
     * @param ts 更新时间
     */
    public void update(BigDecimal px, long ts) {
        this.px = px;
        // 计算当前利润
        this.profitRate = ProfitUtils.getProfitRate(positions, px);

        // 回撤率检查
        if (retrace.profit.compareTo(profitRate) > 0) {
            retrace.profit = profitRate;
            retrace.ts = ts;
        }
        // 最高利润检查
        if (maxProfit.profit.compareTo(profitRate) < 0) {
            maxProfit.profit = profitRate;
            maxProfit.ts = ts;
            if (firstMaxProfit.profit.compareTo(BigDecimal.valueOf(0.1)) >= 0) {
                hasPositiveProfit = false;
            }
        }
        // 首次最高利润检查
        if (hasPositiveProfit) {
            // 如果已经产生正利润，更新最高利润
            if (firstMaxProfit.profit.compareTo(profitRate) < 0) {
                firstMaxProfit.profit = profitRate;
                firstMaxProfit.ts = ts;
            }
        }
    }

}
