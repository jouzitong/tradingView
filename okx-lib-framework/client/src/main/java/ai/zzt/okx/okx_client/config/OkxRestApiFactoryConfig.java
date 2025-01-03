package ai.zzt.okx.okx_client.config;

import ai.zzt.okx.okx_client.properties.OkxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ai.zzt.okx.v5.api.builder.OkxRestApiBuilder;
import ai.zzt.okx.v5.api.pri.AccountApi;
import ai.zzt.okx.v5.api.pri.OrderAlgoApi;
import ai.zzt.okx.v5.api.pri.OrderApi;
import ai.zzt.okx.v5.api.pub.OpenDataApi;

/**
 * @author zhouzhitong
 * @since 2024/5/18
 **/
@Configuration
@Slf4j
public class OkxRestApiFactoryConfig {

    private final OkxProperties okxProperties;

    private OkxRestApiBuilder okxRestApiBuilder;


    public OkxRestApiFactoryConfig(OkxProperties okxProperties) {
        this.okxProperties = okxProperties;
    }


    @Bean
    public OpenDataApi okxPubApi() {
        return builder().createApi(OpenDataApi.class);
    }

    @Component
//    @ConditionalOnProperty(prefix = "okx", name = "syncPrivateTrade",
//            matchIfMissing = true, havingValue = "true")
    public class PrivateApi {
        @Bean
        public AccountApi accountApi() {
            return builder().createApi(AccountApi.class);
        }

        @Bean
        public OrderApi orderApi() {
            return builder().createApi(OrderApi.class);
        }

        @Bean
        public OrderAlgoApi orderAlgoApi() {
            return builder().createApi(OrderAlgoApi.class);
        }

    }

    public OkxRestApiBuilder builder() {
        if (okxRestApiBuilder == null) {
            OkxProperties.Api api = okxProperties.getApi();
            if (api == null) {
                okxRestApiBuilder = new OkxRestApiBuilder(null, null, null, okxProperties.getSimulated());
            } else {
                okxRestApiBuilder = new OkxRestApiBuilder(api.getApiKey(), api.getSecretKey(), api.getPassPhrase(), okxProperties.getSimulated());
            }
        }
        return okxRestApiBuilder;
    }

}
