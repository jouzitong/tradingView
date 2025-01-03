package ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;

/**
 * 策略修改
 */
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAmendRsp extends PolicyDelegationRsp {
    /**
     * 用户自定义修改事件ID，字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间
     */
    @JSONField(name = "reqId")
    @JsonProperty("reqId")
    private String reqId;

}
