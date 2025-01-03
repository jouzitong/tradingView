package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.trade.TgtCcyEnum;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;

/**
 * 策略交易委托 下单请求body基础类
 */
@Data
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDelegationOrderReq implements IOkxRestReq {
    /**
     * 产品ID，如 BTC-USDT
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 交易模式
     */
    @NonNull
    @JSONField(name = "tdMode")
    @JsonProperty("tdMode")
    private TdMode tdMode;

    /**
     * 订单方向
     */
    @NonNull
    @JSONField(name = "side")
    @JsonProperty("side")
    private Side side;

    /**
     * 订单类型
     */
    @NonNull
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    private OrdType ordType;

    /**
     * 保证金币种
     * 仅适用于单币种保证金模式下的全仓杠杆订单
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    private String ccy;

    /**
     * 持仓方向
     * 在开平仓模式下必填，且仅可选择 long 或 short
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    private PositionsSide posSide;

    /**
     * 委托数量
     * sz和closeFraction必填且只能填其一
     */
    @JSONField(name = "sz")
    @JsonProperty("sz")
    private BigDecimal sz;

    /**
     * 订单标签
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字，且长度在1-16位之间
     */
    @JSONField(name = "tag")
    @JsonProperty("tag")
    private String tag;

    /**
     * 委托数量的类型
     * base_ccy: 交易货币 ；quote_ccy：计价货币
     * 仅适用于币币单向止盈止损市价买单
     * 默认买为计价货币，卖为交易货币
     */
    @JSONField(name = "tgtCcy")
    @JsonProperty("tgtCcy")
    private TgtCcyEnum tgtCcy;

    /**
     * 客户自定义策略订单ID
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    private String algoClOrdId;

    /**
     * 策略委托触发时，平仓的百分比。1 代表100%
     * 现在系统只支持全部平仓，唯一接受参数为1
     * 对于同一个仓位，仅支持一笔全部平仓的止盈止损挂单
     * <p>
     * 仅适用于交割或永续
     * 当posSide = net时，reduceOnly必须为true
     * 仅适用于止盈止损 ordType = conditional 或 oco
     * 仅适用于止盈止损市价订单
     * 不支持组合保证金模式
     * sz和closeFraction必填且只能填其一
     */
    @JSONField(name = "closeFraction")
    @JsonProperty("closeFraction")
    private String closeFraction;
}
