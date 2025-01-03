package ai.zzt.okx.v5.entity.rest.policy.trade.rsp;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.entity.rest.IRestEntity;

@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CancelAlgoRsp implements IRestEntity, IOkxRestRsp {
    /**
     * 策略委托单ID
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * 事件执行结果的code，0代表成功
     */
    @JSONField(name = "sCode")
    @JsonProperty("sCode")
    private Integer sCode;

    /**
     * 事件执行失败时的msg
     */
    @JSONField(name = "sMsg")
    @JsonProperty("sMsg")
    private String sMsg;
}
