package ai.zzt.okx.v5.entity.ws.response.pub;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.ws.response.WsChannelResponseArg;
import ai.zzt.okx.v5.enumeration.ws.Channel;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class EstimatedPriceArg implements WsChannelResponseArg {

    @Builder.Default
    @JSONField(name = "channel")
    @JsonProperty("channel")
    private Channel channel = Channel.ESTIMATED_PRICE;

    /**
     * 产品类型
     * FUTURES：交割合约
     * SWAP：永续合约
     * OPTION：期权
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * 交易品种
     * instFamily和instId必须指定一个
     */
    @JSONField(name = "instFamily")
    @JsonProperty("instFamily")
    private String instFamily;


    /**
     * 产品ID
     * instFamily和instId必须指定一个
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

}

