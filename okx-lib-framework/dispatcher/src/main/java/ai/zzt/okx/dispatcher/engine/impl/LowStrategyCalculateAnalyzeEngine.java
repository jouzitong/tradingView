package ai.zzt.okx.dispatcher.engine.impl;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.settings.context.SettingsContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhitong
 * @since 2024/12/13
 **/
//@Service
@Slf4j
//@ConditionalOnMissingBean(ICalculateAnalyzeEngine.class)
public class LowStrategyCalculateAnalyzeEngine extends BaseCalculateAnalyzeEngine {

    @Override
    protected void doAnalyze(AnalyzeContext analyzeContext, SettingsContext settingsContext) {

    }

    @Override
    protected void doAnalyzeClose(AnalyzeContext analyzeContext, SettingsContext settingsContext, PositionContext positionContext) {

    }

}
