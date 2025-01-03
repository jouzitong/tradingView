package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;

/**
 * 撤销策略委托订单
 */
@Data
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CancelAlgoReq implements IOkxRestReq {
    /**
     * 策略委托单ID
     */
    @NonNull
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * 产品ID
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;
}
