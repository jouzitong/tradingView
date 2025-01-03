package ai.zzt.okx.common.web.web;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.api.SystemApi;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * System API
 *
 * @author zhouzhitong
 * @since 2024/10/15
 **/
@RestController
@RequestMapping("/api/v1/systems")
public class SystemController implements SystemApi {

    @Resource
    private AppProperties appProperties;

    @PostMapping("on")
    public R<Boolean> open() {
        SystemContext.open();
        return R.ok(SystemContext.isRunning());
    }

    @GetMapping("/state")
    public R<Boolean> getState() {
        return R.ok(SystemContext.isRunning());
    }

    @PostMapping("/off")
    public R<Boolean> close(@RequestParam(value = "msg", required = false) String msg) {
        if (msg == null) {
            msg = "未知原因...";
        }
        SystemContext.close(msg);
        return R.ok(SystemContext.isRunning());
    }

    @GetMapping("/appConfig")
    public R<AppProperties> getAppConfig() {
        return R.ok(appProperties);
    }

}
