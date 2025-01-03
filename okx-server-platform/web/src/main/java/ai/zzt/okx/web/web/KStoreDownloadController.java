package ai.zzt.okx.web.web;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.web.service.KStoreDownloadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhitong
 * @since 2024/12/19
 **/
@RestController
@RequestMapping("/api/v1/historyData")
public class KStoreDownloadController {

    @Resource
    private KStoreDownloadService service;

    @GetMapping("/file/download")
    public R<Void> download(@RequestParam("instId") String instId,
                            @RequestParam("bar") String bar) {
        service.syncAllFile(InstUtils.getSwapId(instId), Bar.ofVal(bar));
        return R.ok();
    }

    @GetMapping("/file/download/all")
    public R<Void> downloadAll() {
        service.syncAllFile();
        return R.ok();
    }
}
