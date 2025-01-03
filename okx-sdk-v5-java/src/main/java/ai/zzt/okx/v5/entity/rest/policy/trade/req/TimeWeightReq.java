package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 时间加权
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TimeWeightReq extends PolicyDelegationOrderReq {
    /**
     * 吃单价优于盘口的比例，取值范围在 [0.0001,0.01] 之间，如 "0.01"代表"1%"
     * 以买入为例，市价低于限制价时，策略开始用买一价向上取一定比例的委托价来委托小额买单。当前这个参数就用来确定向上的比例。
     * pxVar和pxSpread只能传入一个
     */
    @JSONField(name = "pxVar")
    @JsonProperty("pxVar")
    private BigDecimal pxVar;

    /**
     * 吃单单价优于盘口的价距，取值范围不小于0（无上限）
     * 以买入为例，市价低于限制价时，策略开始用买一价向上取一定价距的委托价来委托小额买单。当前这个参数就用来确定向上的价距。
     */
    @JSONField(name = "pxSpread")
    @JsonProperty("pxSpread")
    private BigDecimal pxSpread;

    /**
     * 单笔数量
     * 以买入为例，市价低于 “限制价” 时，策略开始用买一价向上取一定价距 / 比例的委托价来委托 “一定数量” 的买单。当前这个参数用来确定其中的 “一定数量”。
     */
    @NonNull
    @JSONField(name = "szLimit")
    @JsonProperty("szLimit")
    private BigDecimal szLimit;

    /**
     * 吃单限制价，取值范围不小于0（无上限）
     * 以买入为例，市价低于 “限制价” 时，策略开始用买一价向上取一定价距 / 比例的委托价来委托小额买单。当前这个参数就是其中的 “限制价”。
     */
    @NonNull
    @JSONField(name = "pxLimit")
    @JsonProperty("pxLimit")
    private BigDecimal pxLimit;

    /**
     * 下单间隔，单位为秒。
     * 以买入为例，市价低于 “限制价” 时，策略开始按 “时间周期” 用买一价向上取一定价距 / 比例的委托价来委托小额买单。当前这个参数就是其中的 “时间周期”。
     */
    @NonNull
    @JSONField(name = "timeInterval")
    @JsonProperty("timeInterval")
    private BigDecimal timeInterval;
}
