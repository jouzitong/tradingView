package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

@Data
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlgoInfoOnPendingReq implements IOkxRestReq {
    /**
     * 策略委托单ID
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * Instrument type
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * 产品ID
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 订单类型
     */
    @NonNull
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    private OrdType ordType;

    /**
     * 请求此ID之前（更旧的数据）的分页内容，传的值为对应接口的algoId
     */
    @JSONField(name = "after")
    @JsonProperty("after")
    private String after;

    /**
     * 请求此ID之后（更新的数据）的分页内容，传的值为对应接口的algoId
     */
    @JSONField(name = "before")
    @JsonProperty("before")
    private String before;

    /**
     * 返回结果的数量，最大为100，默认100条
     */
    @JSONField(name = "limit")
    @JsonProperty("limit")
    private Integer limit;

    /**
     * 客户自定义策略订单ID
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    private String algoClOrdId;
}
