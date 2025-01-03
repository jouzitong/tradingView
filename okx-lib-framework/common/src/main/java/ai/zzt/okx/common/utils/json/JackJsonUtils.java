package ai.zzt.okx.common.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhouzhitong
 * @since 2024-03-30
 **/
@Slf4j
@Component
public class JackJsonUtils {

    public final static ObjectMapper JSON = new ObjectMapper();

    static {
        // 允许 json 生成器自动补全未匹配的括号
        JSON.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true);
        // 允许 json 生成器在写入完成后自动关闭写入的流
        // JSON.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        // 允许 json 存在没用引号括起来的 field
        JSON.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许 json 存在使用单引号括起来的 field
        JSON.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许 json 存在没用引号括起来的 ascii 控制字符
        // JSON.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许 json number 类型的数存在前导 0 (like: 0001)
        // JSON.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        // 允许 json 存在 NaN, INF, -INF 作为 number 类型
        JSON.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        // 允许 json 存在形如 // 或 /**/ 的注释
        // JSON.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 序列化时, 禁止自动缩进 (格式化) 输出的 json (压缩输出)
        // JSON.configure(SerializationFeature.INDENT_OUTPUT, false);
        // 序列化时, 将各种时间日期类型统一序列化为 timestamp 而不是其字符串表示
        // JSON.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // 序列化时, 对于没有任何 public methods / properties 的类, 序列化不报错
        JSON.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略未知的字段
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 所有实例中的 空字段, null 字段, 都要参与序列化
        JSON.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 允许基本类型的值为null
        JSON.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        // 控制枚举值是否被允许序列化/反序列化为数字
        // JSON.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        // 可以将结果生成为数组
        JSON.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        JSON.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        JSON.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public static String toStr(Object value) {

        if (null != value) {
            return JSON.writeValueAsString(value);
        }
        return null;
    }

    @SneakyThrows
    public static <T> T toBean(String content, Class<T> valueType) {
        if (null != content) {
            return JSON.readValue(content, valueType);
        }
        return null;
    }

    @SneakyThrows
    public static <T> T toBean(String content, TypeReference<T> valueTypeRef) {
        if (null != content) {
            return JSON.readValue(content, valueTypeRef);
        }
        return null;
    }

    public static <T> T toBean(Map<?, ?> map, Class<T> valueType) {
        return JSON.convertValue(map, valueType);
    }

    public static <T> List<T> toBeanList(Object obj, Class<T> valueType) {
        String content = toStr(obj);
        return toBeanList(content, valueType);
    }

    public static <T> List<T> toBeanList(String content, Class<T> valueType) {
        if (StringUtils.isEmpty(content)) {
            return Collections.EMPTY_LIST;
        }
        try {
            return JSON.readValue(content, JSON.getTypeFactory()
                    .constructCollectionType(List.class, valueType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T clone(T obj) {
        try {
            return (T) JSON.readValue(toStr(obj), obj.getClass());
        } catch (Exception e) {
            log.error("clone error", e);
            throw new RuntimeException(e);
        }
    }

    public static Set<String> readValueToList(InputStream is) {
        try {
            return JSON.readValue(is, Set.class);
        } catch (Exception e) {
            log.error("readValueToList error", e);
        }
        return null;
    }

    public static void main(String[] args) throws JsonProcessingException {
        String s = "{\"opCode\":1003,\"data\":{\"uid\":\"TEST\",\"instId\":\"\",\"bar\":\"1H\",\"startTime\":\"2024-08-01\",\"endTime\":null,\"settingsContext\":\"{\"instId\":\"CRV-USDT-SWAP\",\"instType\":\"SWAP\",\"bars\":[\"5m\"],\"targetWeight\":70,\"calculateSettingsFaceMap\":{\"K line\":{\"indicatorType\":\"K line\",\"enable\":true,\"barSettingsMap\":{\"5m\":{\"weights\":200,\"period\":20,\"lossStrategy\":{\"timePeriod\":60000,\"minDiffRate\":\"0.1\",\"reboundRate\":\"0.02\"}}}}},\"placeOrderSettings\":{\"enablePlaceOrder\":false,\"cash\":100,\"tdMode\":\"cross\",\"openOrderType\":\"market\",\"closeOrderType\":\"market\",\"retraceRate\":0.5,\"lever\":10,\"limit\":{\"maxCount\":1,\"minIntervalTime\":604800000,\"supportTypes\":[\"SPOT\",\"MARGIN\",\"SWAP\"],\"sides\":[\"long\"],\"coolingOffTime\":0,\"openIntervalTime\":28800000,\"maxPrice\":102000,\"minPrice\":null},\"profit\":{\"leverProfit\":false,\"maxProfitRate\":0.05,\"greedyOco\":{\"enable\":true,\"greedyOcoRate\":0.002,\"greedyOcoRateTrigger\":0.002,\"maxTriggerCount\":4},\"minProfitRate\":0.005},\"stopLossRate\":-0.3,\"positionMaxTime\":604800000,\"supportMaxTime\":false,\"supportStopLoss\":false}}\",\"autoPush\":true,\"interval\":1000,\"limit\":100}}";
        JsonNode jsonNode = JSON.readTree(s);
        System.out.println(jsonNode);
    }

}
