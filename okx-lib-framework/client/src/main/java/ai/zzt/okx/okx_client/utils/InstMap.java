package ai.zzt.okx.okx_client.utils;

import ai.zzt.okx.common.utils.InstUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
public class InstMap<V> {

    private final Map<String, V> instMap = new ConcurrentHashMap<>();

    public InstMap() {
    }

    public void put(String instId, V value) {
        instId = InstUtils.getBase(instId);
        instMap.put(instId, value);
    }

    public V get(String instId) {
        instId = InstUtils.getBase(instId);
        return instMap.get(instId);
    }

}
