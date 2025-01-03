package ai.zzt.okx.data.ws.request.vo;

import ai.zzt.okx.common.web.base.ws.BaseArgs;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Setter
@Getter
@ToString(callSuper = true)
public class ChannelParams extends BaseArgs {

    // ==================== channel = tikers ===========

    private String trend;

    // ==================== channel = kline ===========

    private String instId;

    private InstrumentType instType;

    private Bar bar;

//    public void setChannel(String channelName) {
//        this.channel = ChannelType.of(channelName);
//    }
}
