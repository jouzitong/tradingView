package ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import ai.zzt.okx.v5.entity.rest.policy.trade.AttachAlgoOrd;

/**
 * 附带止盈止损信息
 * 适用于单币种保证金模式/跨币种保证金模式/组合保证金模式
 *
 * @author liuxiaokun
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AttachAlgoOrdRep extends AttachAlgoOrd {
    /**
     * 订单 ID
     */
    @JSONField(name = "ordId")
    @JsonProperty("ordId")
    private String ordId;

}
