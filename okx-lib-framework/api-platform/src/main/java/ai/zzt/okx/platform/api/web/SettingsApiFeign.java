package ai.zzt.okx.platform.api.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.settings.context.SettingsContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@FeignClient(name = "${app.platform.name}", url = "${app.platform.url}" + "/api/pri/v1/settings")
public interface SettingsApiFeign {

    @GetMapping("/{instId}")
    R<SettingsContext> getSettings(@PathVariable("instId") String instId);

    @GetMapping("/{instId}/{version}")
    R<SettingsContext> getSettings(@PathVariable("instId") String instId, @PathVariable("version") String version);

    /**
     * 列出所有的配置
     *
     * @return IR
     */
    @GetMapping("/list")
    IR<List<SettingsContext>> list();

}
