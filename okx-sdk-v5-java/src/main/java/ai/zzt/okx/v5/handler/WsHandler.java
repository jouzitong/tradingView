package ai.zzt.okx.v5.handler;

import ai.zzt.okx.v5.ws.OkxWsApiService;

public interface WsHandler {

    void handle(OkxWsApiService okxWsApiService);

}
