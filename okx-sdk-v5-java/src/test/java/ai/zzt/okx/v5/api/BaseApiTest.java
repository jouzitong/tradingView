package ai.zzt.okx.v5.api;

import io.reactivex.rxjava3.core.Single;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.entity.rest.IOkxRestObject;

import java.time.Duration;

/**
 * @author Jou Ziton
 * @since 2024/6/4
 **/
public class BaseApiTest {

    static {
        // 开启代理
        System.setProperty("https.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
    }

    protected <T extends OkxApi> T createOkxOrderAlgoApi(Class<T> t) {
        return OkxRestApiFactory.buildClient("44cad3f1-1deb-4a2c-9ff0-a36857b2bf08", "8537260ABE502FCD1C7F373D308AEB12",
                "ZZt012818..", true, Duration.ofSeconds(10), t);
    }

    protected <T extends IOkxRestObject> T test(Single<T> f) {
        try {
            return OkxRestApiFactory.get(f);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
