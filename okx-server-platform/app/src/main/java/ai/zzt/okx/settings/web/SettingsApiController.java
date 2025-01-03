package ai.zzt.okx.settings.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.platform.api.web.SettingsApiFeign;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.service.impl.SettingsServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 对外提供的api
 *
 * @author zhouzhitong
 * @since 2024/11/27
 **/
@RestController
@RequestMapping("/api/pri/v1/settings")
public class SettingsApiController implements SettingsApiFeign {

    @Resource
    private SettingsServiceImpl settingsService;

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
     * 列出所有的配置
     *
     * @return IR
     */
    @GetMapping("/list")
    public IR<List<SettingsContext>> list() {
        return R.ok(settingsService.list());
    }



}
