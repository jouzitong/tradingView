package ai.zzt.okx.emulator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 利润记录实体
 */
@Data
@NoArgsConstructor
@Document("profit_record")
public class ProfitRecordEntity implements Serializable {

    @Id
    private String id;

    @Field("task_id")
    private String taskId;

    @Deprecated // orderId 应该没有必要存储
    @Field("order_id")
    private String orderId;

    /**
     * 开仓时间
     */
    @Field("open_time")
    private LocalDateTime openTime;

    /**
     * 开仓价格
     */
    @Field("open_mark_price")
    private BigDecimal openMarketPrice;

    /**
     * 平仓时间
     */
    @Field("close_time")
    private LocalDateTime closeTime;

    /**
     * 平仓价格
     */
    @Field("close_mark_price")
    private BigDecimal closeMarkPrice;

    /**
     * 平仓时利润
     */
    @Field("profit")
    private BigDecimal profit;

    /**
     * 最大利润
     */
    @Field("max_profit")
    private BigDecimal maxProfit;

    /**
     * 最大利润 - 时间点
     */
    @Field("max_profit_ts")
    private Long maxProfitTs;

    /**
     * 最大回撤率
     */
    @Field("retrace")
    private BigDecimal retrace;

    /**
     * 最大回撤率 - 时间点
     */
    @Field("retrace_ts")
    private Long retraceTs;

    /**
     * 第几次开仓
     */
    @Field("open_positions_count")
    private int openPositionsCount;
}
