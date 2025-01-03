package ai.zzt.okx.common.web.api;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.v5.enumeration.Bar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/19
 **/
@FeignClient(value = "${app.collector.name}", url = "${app.collector.url}" + "/api/v1/kStores")
public interface KStoreApiFeign {

    /**
     * 获取存储产品信息存储
     *
     * @return 存储产品信息列表
     */
    @GetMapping("/get/{instId}")
    R<InstStoreInfo> getInst(@PathVariable("instId") String instId);

    /**
     * 获取存储产品信息列表
     *
     * @return 存储产品信息列表
     */
    @GetMapping("/list")
    R<List<InstStoreInfo>> getInstList();

    /**
     * 文件下载
     *
     * @param instId   产品Id
     * @param bar      时间周期
     * @param dateTime 时间. 时间格式: yyyy-MM
     * @return 文件字符串
     */
    @GetMapping("/file/download")
    ResponseEntity<byte[]> download(@RequestParam("instId") String instId,
                                    @RequestParam("bar") Bar bar,
                                    @RequestParam("fileName") String dateTime);

}
