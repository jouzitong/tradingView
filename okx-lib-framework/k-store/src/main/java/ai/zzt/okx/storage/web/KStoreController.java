package ai.zzt.okx.storage.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.api.KStoreApiFeign;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/19
 **/
@RestController
@RequestMapping("/api/v1/kStores")
public class KStoreController implements KStoreApiFeign {

    @Resource
    private KStoreService kStoreService;

    /**
     * 获取存储产品信息存储
     *
     * @return 存储产品信息列表
     */
    @GetMapping("/get/{instId}")
    public R<InstStoreInfo> getInst(@PathVariable("instId") String instId) {
        InstStoreInfo inst = kStoreService.getInst(instId);
        return R.ok(inst);
    }

    /**
     * 获取存储产品信息列表
     *
     * @return 存储产品信息列表
     */
    @GetMapping("/list")
    public R<List<InstStoreInfo>> getInstList() {
        return R.ok(kStoreService.getInstList());
    }

    @GetMapping("/file/download")
    public ResponseEntity<byte[]> download(@RequestParam("instId") String instId,
                                           @RequestParam("bar") Bar bar,
                                           @RequestParam("fileName") String dateTime) {
        byte[] fileBytes = kStoreService.getByte(instId, bar, dateTime);
        // 如果文件大于 100MB, 就抛异常
        if (fileBytes.length > 100L * 1024 * 1024) {
            throw new RuntimeException("文件太大");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + dateTime)
                .body(fileBytes);
    }

}
