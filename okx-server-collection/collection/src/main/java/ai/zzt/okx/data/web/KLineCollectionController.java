package ai.zzt.okx.data.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.data.service.impl.KLineCollectionService;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhitong
 * @since 2024/10/3
 **/
@RestController
@RequestMapping("/api/v1/kline")
public class KLineCollectionController {

    @Resource
    private KLineCollectionService kLineCollectionService;

    @GetMapping("start")
    public R<Void> start() {
        kLineCollectionService.collectHistoryData();
        return R.ok();
    }

    @GetMapping("/collect/{instId}")
    public R<Void> collectHistoryDate(@PathVariable("instId") String instId,
                                      @RequestParam(value = "bar", required = false) Bar bar) {
        if (bar == null) {
            bar = Bar.M_1;
        }
        kLineCollectionService.collectHistoryData(instId, bar);
        return R.ok();
    }

}
