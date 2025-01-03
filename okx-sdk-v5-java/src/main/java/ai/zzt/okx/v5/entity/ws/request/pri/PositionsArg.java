package ai.zzt.okx.v5.entity.ws.request.pri;

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
public class PositionsArg implements WsChannelRequestArg {

    @Builder.Default
    @JSONField(name = "channel")
    @JsonProperty("channel")
    private Channel channel = Channel.POSITIONS;

    @NonNull
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * 交易品种
     * 适用于交割/永续/期权
     */
    @JSONField(name = "instFamily")
    @JsonProperty("instFamily")
    private String instFamily;

    /**
     * 产品ID
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 0: 仅根据持仓事件推送数据
     * 2000, 3000, 4000: 根据持仓事件推送，且根据设置的时间间隔定时推送（ms）
     * <p>
     * 若不添加该字段或将其设置为上述合法值以外的其他值，数据将根据事件推送并大约每 5 秒定期推送一次。
     * <p>
     * 使用该字段需严格遵守以下格式。
     * "extraParams": "
     * {
     * \"updateInterval\": \"0\"
     * }
     * "
     */
    @JSONField(name = "extraParams")
    @JsonProperty("extraParams")
    private String extraParams;

}
