package ai.zzt.okx.common.web.api;

import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
// TODO 临时写死请求地址
@FeignClient(name = "${app.trading.name}", url = "${app.trading.url}" + "/api/v1/systems")
public interface SystemApi {

    @PostMapping("on")
    R<Boolean> open();

    // TODO 应该改成 GET 请求
    @GetMapping("/state")
    R<Boolean> getState();

    @PostMapping("off")
    R<Boolean> close(@RequestParam(value = "msg", required = false) String msg);

    @GetMapping("/appConfig")
    R<AppProperties> getAppConfig();

}
