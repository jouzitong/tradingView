package ai.zzt.okx.common.utils;

import ai.zzt.okx.common.enums.SyncType;

/**
 * 同步工具类
 *
 * @author zhouzhitong
 * @since 2024/7/24
 **/
public class SyncUtils {

    public static String sync(String str, SyncType type) {
        return (str + type.name()).intern();
    }

}
