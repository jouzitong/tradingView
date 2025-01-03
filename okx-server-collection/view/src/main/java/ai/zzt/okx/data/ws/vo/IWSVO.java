package ai.zzt.okx.data.ws.vo;


import ai.zzt.okx.common.web.emus.ChannelType;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
public interface IWSVO<T> {

    ChannelType getChannel();

    List<T> getData();

}
