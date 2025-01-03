package ai.zzt.okx.web.web.trading;

import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.api.SystemApi;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.trading.api.TradingRequestApi;
import ai.zzt.okx.trading.dto.InstInfoDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 量化交易 Api controller
 *
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@RestController
@RequestMapping("/pri/api/v1/trading")
@Slf4j
public class TradingApiController {

    @Resource
    private SystemApi systemApi;

    @Resource
    private TradingRequestApi tradingRequestApi;

    @Resource
    private AppProperties appProperties;

    /**
     * 获取系统状态
     *
     * @return 系统状态
     */
    @GetMapping("/system/state")
    public R<Boolean> getState() {
        return systemApi.getState();
    }

    /**
     * 获取系统状态
     *
     * @return 系统状态
     */
    @GetMapping("/system/{state}")
    public R<Boolean> getState(@PathVariable("state") boolean state) {
        log.info("变更量化交易系统: {}", state);
        if (state) {
            return systemApi.open();
        }
        return systemApi.close("由平台服务停止.");
    }


    /**
     * 获取产品配置
     *
     * @param instId 产品ID
     * @return 产品配置
     */
    @GetMapping("/settings/{instId}")
    public R<?> getSettings(@PathVariable("instId") String instId) {
        R<SettingsContext> r = tradingRequestApi.getSettings(instId);
        if (!r.isOk()) {
            return R.fail("当前配置不存在");
        }
        return r;
    }

    /**
     * 同步更新配置
     *
     * @param instId 产品ID
     */
    @PutMapping("/{instId}")
    public R<Void> update(@PathVariable("instId") String instId) {
        log.info("【量化交易系统】更新产品: {}", instId);
        return tradingRequestApi.update(instId, appProperties.getKey());
    }


    /**
     * 新增量化交易币种
     *
     * @param instId 产品ID
     */
    @PostMapping("/{instId}")
    public R<Void> add(@PathVariable("instId") String instId) {
        log.info("【量化交易系统】新增产品: {}", instId);
        return tradingRequestApi.add(instId, appProperties.getKey());
    }

    /**
     * 删除量化交易币种
     *
     * @param instId 产品ID
     */
    @DeleteMapping("/{instId}")
    public R<Void> delete(@PathVariable("instId") String instId) {
        log.info("【量化交易系统】删除产品: {}", instId);
        return tradingRequestApi.delete(instId, appProperties.getKey());
    }

    /**
     * 运行产品列表
     *
     * @return 产品列表
     */
    @GetMapping("/list")
    public R<List<InstInfoDTO>> list() {
        return tradingRequestApi.list();
    }

}
