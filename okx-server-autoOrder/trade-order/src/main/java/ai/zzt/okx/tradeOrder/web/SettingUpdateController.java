package ai.zzt.okx.tradeOrder.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhitong
 * @since 2024/11/20
 **/
@RestController
@RequestMapping("/api/v1/settings/update")
@Slf4j
@Deprecated
public class SettingUpdateController {

    @Resource
    private RequestManager requestManager;

    @Value("${app.key:root@okx}")
    private String key;

    @PatchMapping("/{instId}")
    public R<Void> update(@PathVariable("instId") String instId,
                          @RequestParam("key") String key,
                          @RequestBody SettingsContext settingsContext) {
        if (!this.key.equals(key)) {
            log.warn("[update] key error: {}", key);
            return R.fail();
        }
        requestManager.get(instId).setSettingsContext(settingsContext);
        return R.ok();
    }

    @PostMapping("/enablePlaceOrder/{instId}/{enable}")
    public R<?> updatePlaceOrder(@PathVariable("instId") String instId,
                                 @RequestParam("key") String key,
                                 @PathVariable("enable") Boolean enablePlaceOrder) {
        log.info("{} 下单配置更新: {}", instId, enablePlaceOrder);
        if (!this.key.equals(key)) {
            log.warn("[updatePlaceOrder] key error: {}", key);
            return R.fail("认证不通过");
        }
        requestManager.get(instId).getSettingsContext()
                .getPlaceOrderSettings()
                .setEnablePlaceOrder(enablePlaceOrder);
        log.info("{} 下单配置更新成功", instId);
        return R.ok();
    }

}
