package ai.zzt.okx.okx_client.context.demo;

/**
 * @author zhouzhitong
 * @since 2024/12/11
 **/
import java.math.BigDecimal;

public class PositionPerformance {

    private String positionId; // 仓位 ID
    private String assetName;  // 资产名称
    private BigDecimal currentProfit; // 当前收益
    private BigDecimal totalDrawdown; // 总回撤
    private BigDecimal maxDrawdown; // 最大回撤
    private BigDecimal initialCapital; // 初始资金
    private BigDecimal currentCapital; // 当前资金
    private BigDecimal firstPeakProfit; // 首次产生利润后的最高利润
    private boolean hasPositiveProfit; // 是否已产生首次正利润

    public PositionPerformance(String positionId, String assetName, BigDecimal initialCapital) {
        this.positionId = positionId;
        this.assetName = assetName;
        this.initialCapital = initialCapital;
        this.currentCapital = initialCapital;
        this.currentProfit = BigDecimal.ZERO;
        this.totalDrawdown = BigDecimal.ZERO;
        this.maxDrawdown = BigDecimal.ZERO;
        this.firstPeakProfit = null; // 初始化为空，表示尚未记录
        this.hasPositiveProfit = false; // 尚未产生首次正利润
    }

    /**
     * 更新仓位收益情况
     *
     * @param profit 当前收益（正值为收益，负值为回撤）
     */
    public void update(BigDecimal profit) {
        this.currentProfit = profit;
        currentCapital = currentCapital.add(profit); // 更新当前资金

        if (!hasPositiveProfit && currentCapital.compareTo(initialCapital) > 0) {
            // 首次产生正利润
            hasPositiveProfit = true;
            firstPeakProfit = profit; // 初始化首次正利润后的最高利润
        }

        if (hasPositiveProfit) {
            // 如果已经产生正利润，更新最高利润
            if (firstPeakProfit == null || profit.compareTo(firstPeakProfit) > 0) {
                firstPeakProfit = profit;
            }
        }

        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            // 如果是回撤
            BigDecimal drawdown = profit.abs(); // 回撤记录为正值
            totalDrawdown = totalDrawdown.add(drawdown);
            maxDrawdown = maxDrawdown.max(drawdown); // 更新最大回撤
        }
    }

    /**
     * 计算当前收益率
     *
     * @return 收益率百分比
     */
    public BigDecimal getProfitPercentage() {
        return currentCapital.subtract(initialCapital)
                .divide(initialCapital, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // Getter 方法
    public String getPositionId() {
        return positionId;
    }

    public String getAssetName() {
        return assetName;
    }

    public BigDecimal getCurrentProfit() {
        return currentProfit;
    }

    public BigDecimal getTotalDrawdown() {
        return totalDrawdown;
    }

    public BigDecimal getMaxDrawdown() {
        return maxDrawdown;
    }

    public BigDecimal getInitialCapital() {
        return initialCapital;
    }

    public BigDecimal getCurrentCapital() {
        return currentCapital;
    }

    public BigDecimal getFirstPeakProfit() {
        return firstPeakProfit;
    }

    @Override
    public String toString() {
        return "PositionPerformance{" +
                "positionId='" + positionId + '\'' +
                ", assetName='" + assetName + '\'' +
                ", currentProfit=" + currentProfit +
                ", totalDrawdown=" + totalDrawdown +
                ", maxDrawdown=" + maxDrawdown +
                ", initialCapital=" + initialCapital +
                ", currentCapital=" + currentCapital +
                ", firstPeakProfit=" + firstPeakProfit +
                ", profitPercentage=" + getProfitPercentage() + "%" +
                '}';
    }

    public static void main(String[] args) {
        // 示例
        PositionPerformance position = new PositionPerformance("P123", "BTC", new BigDecimal("10000"));

        // 模拟收益/回撤数据
        position.update(new BigDecimal("-200"));  // 回撤 -200
        position.update(new BigDecimal("-100"));  // 回撤 -100
        position.update(new BigDecimal("500"));   // 收益 +500，首次正利润
        position.update(new BigDecimal("200"));   // 收益 +200
        position.update(new BigDecimal("-300"));  // 回撤 -300
        position.update(new BigDecimal("-300"));  // 回撤 -300
        position.update(new BigDecimal("-300"));  // 回撤 -300
        position.update(new BigDecimal("1000"));  // 收益 +1000

        System.out.println(position);
    }
}