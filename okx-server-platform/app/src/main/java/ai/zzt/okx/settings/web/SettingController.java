package ai.zzt.okx.settings.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.dto.SettingsListDTO;
import ai.zzt.okx.settings.service.impl.SettingsServiceImpl;
import ai.zzt.okx.trading.api.TradingRequestApi;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * Settings Context API
 *
 * @author zhouzhitong
 * @since 2024/10/29
 **/
@Slf4j
@RestController
@RequestMapping("/api/v1/settings")
public class SettingController {

    @Resource
    private SettingsServiceImpl settingsService;

    @Resource
    private OkxProperties okxProperties;

    @Resource
    private TradingRequestApi tradingRequestApi;

    @Resource
    private AppProperties appProperties;

    /**
     * 添加配置
     *
     * @param settingsContext 配置
     * @return 是否成功
     */
    @PostMapping
    public R<Boolean> add(@RequestBody SettingsContext settingsContext) {
        log.info("add settingContext: {}", settingsContext);
        settingsService.add(settingsContext);
        return R.ok(true);
    }

    /**
     * 同步配置
     *
     * @return
     */
    @PostMapping("/syncSettings/{id}")
    public IR<Void> sync(@PathVariable String id) {
        log.info("同步配置: {}", id);
        return tradingRequestApi.update(id +"-USDT-SWAP", appProperties.getKey());
    }

    @PostMapping("/syncAllSettings")
    public R<Void> syncAll() {
        log.info("同步所有配置");
        for (String instId : InstrumentContext.getAllEnableInstrumentsOfId(InstrumentType.SWAP)) {
            tradingRequestApi.update(instId, appProperties.getKey());
        }
        return R.ok();
    }

    @PostMapping("/enablePlaceOrder/{instId}/{enable}")
    public R<Boolean> enablePlaceOrder(@PathVariable("instId") String instId,
                                       @PathVariable("enable") Boolean enable) {
        if (settingsService.updateEnablePlaceOrder(instId, enable)) {
            tradingRequestApi.updatePlaceOrder(instId, appProperties.getKey(), enable);
            log.info("配置同步成功");
        }
        return R.ok(true);
    }


    @GetMapping("/list")
    public IR<List<SettingsListDTO>> getAllSettingsList() {
        List<SettingsListDTO> list = settingsService.getList();
        return R.ok(list);
    }

    /**
     * 根据币种获取配置上下文
     *
     * @param instId 币种ID
     * @return 配置上下文
     */
    @GetMapping("/{instId}")
    public R<SettingsContext> getSettings(@PathVariable("instId") String instId) {
        return R.ok(settingsService.getSettings(instId));
    }

    @GetMapping("/default")
    public R<SettingsContext> getDefaultSettings() {
        return R.ok(new SettingsContext("TEST", true));
    }

    /**
     * 根据币种获取配置上下文
     *
     * @param instId  币种ID
     * @param version 版本
     * @return 配置上下文
     */
    @GetMapping("/{instId}/{version}")
    public R<SettingsContext> getSettings(@PathVariable("instId") String instId, @PathVariable("version") String version) {
        return R.ok(settingsService.getSettings(instId, version));
    }

    /**
     * 应用所有的配置
     *
     * @param sc 配置上下文 {@link SettingsContext}
     * @return 影响行数
     */
    @PostMapping("/applyAll")
    public IR<Integer> applyAll(@RequestBody SettingsContext sc) {
        log.info("apply all settings: {}", sc);
        List<String> allEnableInst = InstrumentContext.getAllEnableInstrumentsOfId(InstrumentType.SPOT);
        Set<String> hotInstIdList = okxProperties.getHotInstIdList();
        for (String instId : allEnableInst) {
            SettingsContext settingsContext = sc.clone();
            settingsContext.setInstId(instId);
            settingsService.add(settingsContext);
        }
        return R.ok(hotInstIdList.size());
    }

}
