package ai.zzt.okx.common.web.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@FeignClient(name = "${app.name}", url = "${app.${app.name}.url}"+"/api/v1/global/")
public interface GlobalApi {
}
