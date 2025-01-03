package ai.zzt.okx.v5.entity.ws.pub;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.entity.ws.WsSubscribeEntity;
import ai.zzt.okx.v5.enumeration.Alias;
import ai.zzt.okx.v5.enumeration.CtType;
import ai.zzt.okx.v5.enumeration.State;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Instruments implements WsSubscribeEntity, IOkxRestRsp {

    /**
     * 产品类型
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * 产品ID，如 LTC-USD-SWAP
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 标的指数，如 BTC-USD，仅适用于杠杆/交割/永续/期权
     */
    @JSONField(name = "uly")
    @JsonProperty("uly")
    private String uly;

    /**
     * 交易品种，如 BTC-USD，仅适用于杠杆/交割/永续/期权
     */
    @JSONField(name = "instFamily")
    @JsonProperty("instFamily")
    private String instFamily;

    /**
     * 币种类别（已废弃）
     */
    @Deprecated
    @JSONField(name = "category")
    @JsonProperty("category")
    private String category;

    /**
     * 交易货币币种，如 BTC-USDT 中的 BTC ，仅适用于币币/币币杠杆
     */
    @JSONField(name = "baseCcy")
    @JsonProperty("baseCcy")
    private String baseCcy;

    /**
     * 计价货币币种，如 BTC-USDT 中的USDT ，仅适用于币币/币币杠杆
     */
    @JSONField(name = "quoteCcy")
    @JsonProperty("quoteCcy")
    private String quoteCcy;

    /**
     * 盈亏结算和保证金币种，如 BTC 仅适用于交割/永续/期权
     */
    @JSONField(name = "settleCcy")
    @JsonProperty("settleCcy")
    private String settleCcy;

    /**
     * 合约面值，仅适用于交割/永续/期权
     */
    @JSONField(name = "ctVal")
    @JsonProperty("ctVal")
    private BigDecimal ctVal;

    /**
     * 合约乘数，仅适用于交割/永续/期权
     */
    @JSONField(name = "ctMult")
    @JsonProperty("ctMult")
    private String ctMult;

    /**
     * 合约面值计价币种，仅适用于交割/永续/期权
     */
    @JSONField(name = "ctValCcy")
    @JsonProperty("ctValCcy")
    private String ctValCcy;

    /**
     * 期权类型，C或P 仅适用于期权
     */
    @JSONField(name = "optType")
    @JsonProperty("optType")
    private String optType;


    /**
     * 	行权价格，仅适用于期权
     */
    @JSONField(name = "stk")
    @JsonProperty("stk")
    private String stk;

    /**
     * 	上线时间
     * Unix时间戳的毫秒数格式，如 1597026383085
     */
    @JSONField(name = "listTime")
    @JsonProperty("listTime")
    private Long listTime;

    /**
     * 	产品下线时间
     * 适用于币币/杠杆/交割/永续/期权，对于 交割/期权，为交割/行权日期；亦可以为产品下线时间，有变动就会推送。
     */
    @JSONField(name = "expTime")
    @JsonProperty("expTime")
    private Long expTime;

    /**
     * 	该instId支持的最大杠杆倍数，不适用于币币、期权
     */
    @JSONField(name = "lever")
    @JsonProperty("lever")
    private BigDecimal lever;

    /**
     * 	下单价格精度，如 0.0001
     * 对于期权来说，是梯度中的最小下单价格精度，如果想要获取期权价格梯度，请使用"获取期权价格梯度"接口
     */
    @JSONField(name = "tickSz")
    @JsonProperty("tickSz")
    private BigDecimal tickSz;


    /**
     * 	下单数量精度
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "lotSz")
    @JsonProperty("lotSz")
    private BigDecimal lotSz;

    /**
     * 	最小下单数量
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "minSz")
    @JsonProperty("minSz")
    private BigDecimal minSz;


    /**
     *	合约类型
     * linear：正向合约
     * inverse：反向合约
     * 仅适用于交割/永续
     */
    @JSONField(name = "ctType")
    @JsonProperty("ctType")
    private CtType ctType;


    /**
     *仅适用于交割
     * 不建议使用，用户应通过 expTime 字段获取合约的交割日期
     */
    @JSONField(name = "alias")
    @JsonProperty("alias")
    private Alias alias;

    /**
     *产品状态
     */
    @JSONField(name = "state")
    @JsonProperty("state")
    private State state;


    /**
     * 限价单的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "maxLmtSz")
    @JsonProperty("maxLmtSz")
    private BigDecimal maxLmtSz;

    /**
     * 市价单的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是USDT
     */
    @JSONField(name = "maxMktSz")
    @JsonProperty("maxMktSz")
    private BigDecimal maxMktSz;

    /**
     * 限价单的单笔最大美元价值
     */
    @JSONField(name = "maxLmtAmt")
    @JsonProperty("maxLmtAmt")
    private BigDecimal maxLmtAmt;


    /**
     * 市价单的单笔最大美元价值
     * 仅适用于币币/币币杠杆
     */
    @JSONField(name = "maxMktAmt")
    @JsonProperty("maxMktAmt")
    private BigDecimal maxMktAmt;

    /**
     *  时间加权单的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "maxTwapSz")
    @JsonProperty("maxTwapSz")
    private BigDecimal maxTwapSz;

    /**
     *  冰山委托的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "maxIcebergSz")
    @JsonProperty("maxIcebergSz")
    private BigDecimal maxIcebergSz;

    /**
     * 计划委托委托的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是交易货币
     */
    @JSONField(name = "maxTriggerSz")
    @JsonProperty("maxTriggerSz")
    private BigDecimal maxTriggerSz;

    /**
     * 	止盈止损市价委托的单笔最大委托数量
     * 合约的数量单位是张，现货的数量单位是USDT
     */
    @JSONField(name = "maxStopSz")
    @JsonProperty("maxStopSz")
    private BigDecimal maxStopSz;

}
