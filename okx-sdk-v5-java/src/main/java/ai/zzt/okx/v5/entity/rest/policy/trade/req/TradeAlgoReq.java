package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;

/**
 * 策略交易委托 关于有单据的基础类
 */
@Data
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TradeAlgoReq implements IOkxRestReq {
    /**
     * 产品ID 如 BTC-USDT
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 策略委托单ID
     */
    @NonNull
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;
}
