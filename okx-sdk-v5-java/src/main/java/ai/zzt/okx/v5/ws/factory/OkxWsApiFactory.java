package ai.zzt.okx.v5.ws.factory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static ai.zzt.okx.v5.constant.OkxConstants.*;

/**
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public class OkxWsApiFactory {

    /**
     * 创建默认请求
     *
     * @param wsChannel wsChannel
     * @param simulated 是否模拟请求
     * @return 请求
     */
    public static Request defaultRequest(WsChannel wsChannel, boolean simulated) {
        return new Request.Builder().url(switch (wsChannel) {
            case PUBLIC -> simulated ? SIM_WS_PUBLIC_URL : WS_PUBLIC_URL;
            case PRIVATE -> simulated ? SIM_WS_PRIVATE_URL : WS_PRIVATE_URL;
            case BUSINESS -> simulated ? SIM_WS_BUSINESS_URL : WS_BUSINESS_URL;
        }).build();
    }

    /**
     * 默认的 OkHttpClient
     *
     * @return OkHttpClient
     */
    public static OkHttpClient defaultClient(Duration pingInterval) {
        return new OkHttpClient.Builder()
                .pingInterval(pingInterval)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

}
