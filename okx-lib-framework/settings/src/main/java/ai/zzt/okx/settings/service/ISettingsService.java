package ai.zzt.okx.settings.service;

import ai.zzt.okx.settings.context.SettingsContext;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
public interface ISettingsService {

    default boolean add(SettingsContext context) {
        return false;
    }

    /**
     * 获取合约配置
     *
     * @param instId 合约ID
     * @return 合约配置
     */
    default SettingsContext getSettings(String instId) {
        return getSettings(instId, null);
    }

    /**
     * 获取配置上下文
     *
     * @param instId  合约ID
     * @param version 应用版本
     * @return 配置上下文
     */
    default SettingsContext getSettings(String instId, String version) {
        return null;
    }

    boolean remove(String instId, String version);

}
