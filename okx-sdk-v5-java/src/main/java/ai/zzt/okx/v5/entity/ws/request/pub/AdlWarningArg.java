package ai.zzt.okx.v5.entity.ws.request.pub;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.ws.request.WsChannelRequestArg;
import ai.zzt.okx.v5.enumeration.ws.Channel;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;


@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdlWarningArg implements WsChannelRequestArg {

    @Builder.Default
    @JSONField(name = "channel")
    @JsonProperty("channel")
    private Channel channel = Channel.ADL_WARNINGS;

    /**
     * 产品类型
     * FUTURES：交割合约
     * SWAP：永续合约
     * OPTION：期权
     */
    @NonNull
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * 交易品种
     */
    @JSONField(name = "instFamily")
    @JsonProperty("instFamily")
    private String instFamily;

}
