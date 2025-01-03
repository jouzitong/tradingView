package ai.zzt.okx.web.service;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.common.web.api.KStoreApiFeign;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/19
 **/
@Service
@Slf4j
public class KStoreDownloadService {


    @Resource
    private KStoreApiFeign kStoreApi;

    @Resource
    private KStoreService kStoreService;

    public void syncAllFile() {
        ThreadUtils.runIO(() -> {
            R<List<InstStoreInfo>> r = kStoreApi.getInstList();
            if (!r.isOk()) {
                throw new TodoRuntimeException(r.getMsg());
            }
            for (InstStoreInfo instStoreInfo : r.getData()) {
                process(instStoreInfo.getInstId(), Bar.M_1, instStoreInfo);
            }
        });
    }

    public void syncAllFile(String instId, Bar bar) {
        log.debug("同步 {} 数据[{}]", instId, bar);
        ThreadUtils.runIO(() -> {
            R<InstStoreInfo> r = kStoreApi.getInst(instId);
            if (!r.isOk()) {
                throw new TodoRuntimeException(r.getMsg());
            }
            InstStoreInfo remoteStore = r.getData();
            process(instId, bar, remoteStore);
        });
    }

    private void process(String instId, Bar bar, InstStoreInfo remoteStore) {
        InstStoreInfo localStore = kStoreService.getInst(instId);
        if (localStore == null) {
            List<String> fileNames = remoteStore.getInterval(bar).getExtraMonths();
            download(instId, bar, fileNames);
            return;
        }
        List<String> fileNames = remoteStore.getInterval(bar).getExtraMonths(localStore.getInterval(bar));
        log.debug("产品 {} 需要同步列表: {}", instId, fileNames);
        download(instId, bar, fileNames);
    }

    protected void download(String instId, Bar bar, List<String> fileNames) {
        for (String fileName : fileNames) {
            download(instId, bar, fileName);
        }
    }

    public void download(String instId, Bar bar, String fileName) {
        log.debug("文件下载: {}-{}-{}", instId, bar, fileName);
        try {
            ResponseEntity<byte[]> response = kStoreApi.download(instId, bar, fileName);
            if (response.getStatusCode().is2xxSuccessful()) {
                byte[] fileBytes = response.getBody();
                kStoreService.saveFile(instId, bar, fileName, fileBytes);
            } else {
                log.error("文件存储异常: {}", response);
            }
        } catch (Exception e) {
            log.error("文件下载异常. instId: {}, bar: {}, fileName: {}. ", instId, bar, fileName, e);
        }
    }

}
