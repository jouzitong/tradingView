package ai.zzt.okx.data.ws.vo;

import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.web.emus.ChannelType;
import lombok.Data;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Data
public class ChanelVO<T> implements IWSVO<T> {

    private ChannelType channel;

    private TrendType trend;

    private List<T> data;

}
