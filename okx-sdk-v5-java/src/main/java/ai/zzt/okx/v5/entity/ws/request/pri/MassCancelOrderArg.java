package ai.zzt.okx.v5.entity.ws.request.pri;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.ws.request.WsRequestArg;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MassCancelOrderArg implements WsRequestArg {

    /**
     * Instrument type
     * OPTION
     */
    @NonNull
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * Instrument family
     */
    @JSONField(name = "instFamily")
    @JsonProperty("instFamily")
    private String instFamily;

}
