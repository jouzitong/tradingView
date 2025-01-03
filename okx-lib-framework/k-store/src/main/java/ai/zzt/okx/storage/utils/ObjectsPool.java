package ai.zzt.okx.storage.utils;

import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

/**
 * @author zhouzhitong
 * @since 2024/6/23
 **/
public class ObjectsPool {


    public static <T> T get(Class<T> clazz) {
        if (clazz == MarkPrice.class) {
            return (T) new MarkPrice();
        }
        return null;
    }

    public static void release(Object obj) {

    }

}
