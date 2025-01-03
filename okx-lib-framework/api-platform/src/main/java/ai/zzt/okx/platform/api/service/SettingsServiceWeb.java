package ai.zzt.okx.platform.api.service;

import ai.zzt.okx.common.exception.ErrCode;
import ai.zzt.okx.common.exception.base.BaseRuntimeException;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.platform.api.web.SettingsApiFeign;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.service.ISettingsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhitong
 * @since 2024/9/27
 **/
@Slf4j
public class SettingsServiceWeb implements ISettingsService {


    @Resource
    private AppProperties appProperties;

    @Resource
    private SettingsApiFeign settingsApiFeign;

    @Override
    public SettingsContext getSettings(String instId, String version) {
        if (version == null) {
            version = appProperties.getVersion();
        }
        R<SettingsContext> res = settingsApiFeign.getSettings(instId, version);
        if (res == null || !res.isOk()) {
            throw new BaseRuntimeException(new ErrCode(10001, "找不到配置上下文"));
        }
        return res.getData();
    }

    @Override
    public boolean remove(String instId, String version) {
//        CACHE_MAP.remove(instId);
        return false;
    }
}
