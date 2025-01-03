package ai.zzt.okx.v5.api.builder;

import ai.zzt.okx.v5.api.OkxApi;
import ai.zzt.okx.v5.api.OkxPriApi;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;

import java.time.Duration;

/**
 * okx rest api builder
 *
 * @author Jouziton
 * @since 2024/5/16
 **/
public class OkxRestApiBuilder {

    /**
     * default timeout
     */
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    /**
     * api key
     */
    private final String apiKey;

    /**
     * secret key
     */
    private final String secretKey;

    /**
     * passphrase
     */
    private final String passphrase;

    /**
     * simulated
     */
    private final boolean simulated;

    private final Duration timeout;

    public OkxRestApiBuilder(String apiKey, String secretKey, String passphrase, boolean simulated) {
        this(apiKey, secretKey, passphrase, simulated, DEFAULT_TIMEOUT);
    }

    public OkxRestApiBuilder(String apiKey, String secretKey, String passphrase, boolean simulated, Duration timeout) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
        this.simulated = simulated;
        this.timeout = timeout;
    }

    public <T extends OkxApi> T createApi(Class<T> clazz) {
        // clazz is an interface that extends OkxPriApi
        if (OkxPriApi.class.isAssignableFrom(clazz)) {
            return OkxRestApiFactory.buildClient(apiKey, secretKey, passphrase, simulated, timeout, clazz);
        } else {
            return OkxRestApiFactory.buildClient(timeout, clazz);
        }
    }

}
