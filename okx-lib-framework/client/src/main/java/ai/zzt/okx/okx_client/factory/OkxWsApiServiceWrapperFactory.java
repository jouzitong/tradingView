package ai.zzt.okx.okx_client.factory;

import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhitong
 * @since 2024/5/19
 **/
@Component
@Slf4j
public class OkxWsApiServiceWrapperFactory implements FactoryBean<OkxWsApiServiceWrapper> {

    private final OkxProperties okxProperties;

    public OkxWsApiServiceWrapperFactory(OkxProperties okxProperties) {
        this.okxProperties = okxProperties;
    }

    @Override
    public OkxWsApiServiceWrapper getObject() throws Exception {
        Boolean simulated = okxProperties.getSimulated();
        log.info("OKX 环境: {}", simulated ? "模拟环境" : "真实环境");
        return createOkxWsService(simulated);
    }

    @Override
    public Class<?> getObjectType() {
        return OkxWsApiServiceWrapper.class;
    }

    private OkxWsApiServiceWrapper createOkxWsService(boolean simulated) {
        return new OkxWsApiServiceWrapper(simulated, okxProperties);
    }

}
