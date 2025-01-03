package ai.zzt.okx.common.utils;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author zhouzhitong
 * @since 2024/8/5
 **/
public class UnionMap<K, V> implements Map<K, V> {

    /**
     * 存储map. 默认使用HashMap(不安全)
     */
    private final Map<K, V> map;

    private final Map<Object, K> unionMap;

    public UnionMap() {
        this(false);
    }

    public UnionMap(boolean safe) {
        if (safe) {
            map = new HashMap<>();
            unionMap = new HashMap<>();
        } else {
            map = new ConcurrentHashMap<>();
            unionMap = new ConcurrentHashMap<>();
        }
    }

    public void union(Object k, K key) {
        unionMap.put(k, key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key) || unionMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V v = map.get(key);
        if (v != null) {
            return v;
        }
        K k = unionMap.get(key);
        if (k == null) {
            return null;
        }
        return map.get(k);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        K k = unionMap.remove(key);
        if (k == null) {
            return map.remove(key);
        }
        return map.remove(k);
    }

    @Override
    public void clear() {
        map.clear();
        ;
        unionMap.clear();
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        throw new TodoRuntimeException();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        throw new TodoRuntimeException();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        throw new TodoRuntimeException();

    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new TodoRuntimeException();
    }
}
