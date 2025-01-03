package ai.zzt.okx.v5.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jou Ziton
 * @since 2024/5/17
 **/
public class JSONUtils {

    private static final ObjectMapper JSON = new ObjectMapper();

    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        JSON.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CASE);
    }

    public static ObjectMapper objectMapper() {
        return JSON;
    }

    public static String toJsonStr(Object o) {
        try {
            return JSON.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(Object o, Class<T> t) {
        try {
            return JSON.readValue(toJsonStr(o), t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String jsonValue, Class<T> t) {
        try {
            return JSON.readValue(jsonValue, t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
