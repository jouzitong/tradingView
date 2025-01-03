package ai.zzt.okx.v5.api.factory;

import ai.zzt.okx.v5.api.OkxApi;
import ai.zzt.okx.v5.entity.rest.IOkxRestObject;
import ai.zzt.okx.v5.entity.rest.OkeHttpException;
import ai.zzt.okx.v5.entity.rest.OkxRestError;
import ai.zzt.okx.v5.interceptor.AuthenticationInterceptor;
import ai.zzt.okx.v5.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ai.zzt.okx.v5.constant.OkxConstants.BASE_URL;
import static ai.zzt.okx.v5.constant.OkxConstants.SIM_BASE_URL;

/**
 * @author Jouziton
 * @since 2024/5/16
 **/
@Slf4j
public class OkxRestApiFactory {

    private static final Map<Integer, ErrorHandler> errorHandlers = new HashMap<>();

    private static final Set<Integer> retryCodes = new HashSet<>();

    public static void addErrorHandler(ErrorHandler errorHandler) {
        errorHandlers.put(errorHandler.code(), errorHandler);
    }

    public static <T extends OkxApi> T buildClient(Duration timeout, Class<T> t) {
        ObjectMapper mapper = JSONUtils.objectMapper();
        OkHttpClient client = defaultClient(timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper, false);
        return retrofit.create(t);
    }

    public static <T extends OkxApi> T buildClient(String apiKey, String secretKey, String passphrase, boolean simulated, Duration timeout, Class<T> t) {
        ObjectMapper mapper = JSONUtils.objectMapper();
        OkHttpClient client = defaultClient(apiKey, secretKey, passphrase, simulated, timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper, simulated);
        return retrofit.create(t);
    }

    public static OkHttpClient defaultClient(String apiKey, String secretKey, String passphrase, boolean simulated, Duration timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(apiKey, secretKey, passphrase, simulated))
                .connectionPool(new ConnectionPool(10, 4, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();

    }

    public static OkHttpClient defaultClient(Duration timeout) {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 4, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();

    }

    public static Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper, boolean simulated) {
        return new Retrofit.Builder()
                .baseUrl(simulated ? SIM_BASE_URL : BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public static Map<String, Object> paramToMap(Object o) {
        return JSONUtils.readValue(o, Map.class);
    }

    /**
     * Calls the Open AI api, returns the response, and parses error messages if the request fails
     */
    public static <T extends IOkxRestObject<?>> T get(Single<T> apiCall) throws OkeHttpException {
        try {
            T t = apiCall.blockingGet();
            if (!t.isOk()) {
                int code = t.getCode();
                ErrorHandler errorHandler = errorHandlers.get(code);
                if (errorHandler != null) {
                    errorHandler.handler(t, null);
                } else {
                    if (retryCodes.add(code)) {
                        log.warn("Unhandled error code: {}", code);
                    }
                }
            }
            return t;
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();
                OkxRestError error = JSONUtils.readValue(errorBody, OkxRestError.class);
                throw new OkeHttpException(error, error.getMsg(), e);
            } catch (IOException ex) {
                // couldn't parse OpenAI error
                throw e;
            }
        }
    }

}
