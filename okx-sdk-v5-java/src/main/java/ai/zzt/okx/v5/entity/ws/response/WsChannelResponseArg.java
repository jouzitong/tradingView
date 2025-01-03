package ai.zzt.okx.v5.entity.ws.response;

import ai.zzt.okx.v5.enumeration.ws.Channel;

/**
 * The interface of websocket channel argument
 */
public interface WsChannelResponseArg extends WsResponseArg {

    Channel getChannel();

}
