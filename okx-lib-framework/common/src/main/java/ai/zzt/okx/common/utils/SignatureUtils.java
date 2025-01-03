package ai.zzt.okx.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名工具类
 *
 * @author zhouzhitong
 * @since 2024/12/15
 **/
public class SignatureUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 使 JSON 字符串有确定的顺序，保证签名结果一致
        OBJECT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    /**
     * 对象签名方法
     *
     * @param object 要签名的对象
     * @return 对象签名字符串
     * @throws JsonProcessingException 如果序列化出错
     * @throws NoSuchAlgorithmException 如果无法找到指定的哈希算法
     */
    public static String generateSignature(Object object) throws JsonProcessingException, NoSuchAlgorithmException {
        // 1. 将对象序列化为标准的 JSON 字符串
        String normalizedJson = normalizeObject(object);

        // 2. 使用 SHA-256 对 JSON 字符串进行签名
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(normalizedJson.getBytes(StandardCharsets.UTF_8));

        // 3. 将哈希值转为十六进制字符串
        return bytesToHex(hash);
    }

    /**
     * 对象规范化：将对象中的所有字段排序并格式化
     *
     * @param object 输入对象
     * @return 规范化的 JSON 字符串
     * @throws JsonProcessingException 如果序列化出错
     */
    private static String normalizeObject(Object object) throws JsonProcessingException {
        // 将对象转为 JSON 树结构
        ObjectNode objectNode = OBJECT_MAPPER.valueToTree(object);

        // 递归规范化
        Map<String, Object> sortedMap = sortAndNormalize(objectNode);

        // 将排序后的 Map 转为 JSON 字符串
        return OBJECT_MAPPER.writeValueAsString(sortedMap);
    }

    /**
     * 递归地将 ObjectNode 排序
     *
     * @param objectNode 输入的 ObjectNode
     * @return 排序后的 Map
     */
    private static Map<String, Object> sortAndNormalize(ObjectNode objectNode) {
        Map<String, Object> sortedMap = new TreeMap<>();
        objectNode.fields().forEachRemaining(entry -> {
            if (entry.getValue().isObject()) {
                // 递归处理嵌套对象
                sortedMap.put(entry.getKey(), sortAndNormalize((ObjectNode) entry.getValue()));
            } else if (entry.getValue().isNumber()) {
                // 对浮点数格式化，避免精度问题
                sortedMap.put(entry.getKey(), entry.getValue().asDouble());
            } else {
                // 其他类型直接存储
                sortedMap.put(entry.getKey(), entry.getValue());
            }
        });
        return sortedMap;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 输入字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
