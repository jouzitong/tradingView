package ai.zzt.okx.v5.entity.ws.request.pri;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.ws.request.WsChannelRequestArg;
import ai.zzt.okx.v5.enumeration.ws.Channel;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AccountGreeksArg implements WsChannelRequestArg {

    @Builder.Default
    @JSONField(name = "channel")
    @JsonProperty("channel")
    private Channel channel = Channel.ACCOUNT_GREEKS;

    /**
     * 币种
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    private String ccy;


}
