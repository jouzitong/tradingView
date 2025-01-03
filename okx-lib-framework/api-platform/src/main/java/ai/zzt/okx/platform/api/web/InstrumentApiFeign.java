package ai.zzt.okx.platform.api.web;

import ai.zzt.okx.common.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/16
 **/
@FeignClient(name = "${app.platform.name}", url = "${app.platform.url}" + "/open/api/v1/instruments")
public interface InstrumentApiFeign {

    /**
     * 获取可用上线的产品ID
     *
     * @return 需要上线的产品ID
     */
    @GetMapping("/online/instList")
    R<List<String>> getAllOnlineInst();

}
