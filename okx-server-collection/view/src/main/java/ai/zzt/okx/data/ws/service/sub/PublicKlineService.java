package ai.zzt.okx.data.ws.service.sub;

import ai.zzt.okx.data.ws.context.WsClientContext;
import ai.zzt.okx.data.ws.service.BasePublicWsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Service
@Slf4j
public class PublicKlineService extends BasePublicWsService<Object> {


    @Override
    protected Object doService(WsClientContext wsContext) {
        return null;
    }
}
