package ai.zzt.okx.common.utils;

import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/10/20
 **/
public class InstUtils {

    public static final String SPIT = "-";

    private static final Map<String, String> cache_base_map = new ConcurrentHashMap<>();

    public static String getBase(String instId) {
        String res = cache_base_map.get(instId);
        if (res != null) {
            return cache_base_map.get(instId);
        } else {
            res = instId.split(SPIT)[0];
            cache_base_map.put(instId, res);
            return res;
        }
    }

    public static String getSwapId(String id) {
        if (!id.contains("-")) {
            id = id + "-USDT-SWAP";
        }
        return id;
    }

    public static InstrumentType getType(String instId) {
        String[] split = instId.split(SPIT);
        if (split.length <= 2) {
            return InstrumentType.SPOT;
        }
        if (InstrumentType.SWAP.value().equals(split[2])) {
            return InstrumentType.SWAP;
        }
        if (InstrumentType.MARGIN.value().equals(split[2])) {
            return InstrumentType.MARGIN;
        }
        // 默认: 币币
        return InstrumentType.SPOT;
    }

    public static boolean equals(String base, String target) {
        return target.startsWith(base);
    }

}
