package ai.zzt.okx.storage.service.impl;

import ai.zzt.okx.common.enums.SyncType;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SyncUtils;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.storage.bean.Customer;
import ai.zzt.okx.storage.bean.LocalCustomer;
import ai.zzt.okx.storage.config.KStoreProperties;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.storage.utils.ByteBufferPool;
import ai.zzt.okx.storage.utils.FileUtils;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wyz
 */
@Slf4j
@Data
@Service
public class KStoreServiceImpl implements KStoreService {

    private ThreadPoolExecutor readThreadPool;

    @Autowired
    private KStoreProperties kStoreConfig;

    private ByteBufferPool bbPool;

    public KStoreServiceImpl() {
        // 读线程池创建
        readThreadPool = new ThreadPoolExecutor(2,
                2, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), new CustomizableThreadFactory("k-store-read-thread"),
                new ThreadPoolExecutor.AbortPolicy());

        bbPool = new ByteBufferPool(10_0000, FileUtils.bufferSize(K.class) * 256);
    }

    @Override
    public void store(String instId, Bar bar, K k) {
        long ts = k.getTs();
        LocalDate current = DateTimeUtils.ofTime(ts).toLocalDate();
        synchronized (SyncUtils.sync(instId, SyncType.STORE_DATA)) {
            String filePath = createFileName(instId, bar, current);
            ByteBuffer buffer = bbPool.get();
            try {
                FileUtils.write(filePath, k, buffer);
            } finally {
                bbPool.clear(buffer);
            }
        }
    }

//    @Override
//    public void store(String instId, Bar bar, List<K> ks) {
//        if (CollectionUtils.isEmpty(ks)) {
//            return;
//        }
//        ByteBuffer buffer = bbPool.get();
//        Long lastTs = null;
//        for (K k : ks) {
//            if (lastTs == null) {
//                lastTs = k.getTs();
//            }
//        }
//
//    }

    @Override
    public Customer<MarkPrice> createCustomer(Bar bar, String instId) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");

        // 存在性判断
        if (!targetDir.exists()) {
            throw new RuntimeException(STR."目标文件夹不存在:\{targetDir.getAbsolutePath()}");
        }

        File[] files = targetDir.listFiles();
        if (files != null && files.length > 0) {
            // 日期排序，最早的在前
            Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

            LocalCustomer customer = new LocalCustomer();
            ThreadUtils.runIO(() -> {
                fileTag:
                for (File file : files) {
                    ByteBuffer buffer = bbPool.get();
                    try (FileInputStream fis = new FileInputStream(file);
                         FileChannel channel = fis.getChannel()) {
                        while (true) {
                            List<K> ks = FileUtils.read(channel, buffer);
                            if (CollectionUtils.isEmpty(ks)) {
                                break;
                            }
                            List<MarkPrice> mps = new ArrayList<>();
                            for (K k : ks) {
                                mps.addAll(k.toMp(instId, bar));
                            }
                            customer.putAll(mps);
                            // 提前结束读取
                            if (customer.isEnd()) {
                                break fileTag;
                            }
                        }
                    } catch (Exception e) {
                        log.error("数据读取失败:{}.", instId, e);
                    } finally {
                        bbPool.clear(buffer);
                    }
                }
                // 结束标识
                customer.getEnd().set(true);
            });
            return customer;
        }
        return LocalCustomer.empty();
    }

    @Override
    public Customer<MarkPrice> createCustomer(Bar bar, String instId, long st) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");

        // 存在性判断
        if (!targetDir.exists()) {
            throw new RuntimeException(STR."目标文件夹不存在:\{targetDir.getAbsolutePath()}");
        }

        File[] files = targetDir.listFiles();
        if (files != null && files.length > 0) {
            // 日期排序，最早的在前
            Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            LocalCustomer customer = new LocalCustomer();
            ThreadUtils.runIO(() -> {
                LocalDateTime localDateTime = DateTimeUtils.ofTime(st);
                // 截取前7个字符
                String tsStr = localDateTime.toString().substring(0, 7);
                fileTag:
                for (File file : files) {
                    String name = file.getName();
                    if (name.compareToIgnoreCase(tsStr) < 0) {
                        continue;
                    }
                    ByteBuffer buffer = bbPool.get();
                    try (FileInputStream fis = new FileInputStream(file);
                         FileChannel channel = fis.getChannel()) {
                        while (true) {
                            List<K> ks = FileUtils.read(channel, buffer);
                            if (CollectionUtils.isEmpty(ks)) {
                                break;
                            }
                            List<MarkPrice> mps = new ArrayList<>();
                            for (K k : ks) {
                                mps.addAll(k.toMp(instId, bar));
                            }
                            customer.putAll(mps);
                            // 提前结束读取
                            if (customer.isEnd()) {
                                break fileTag;
                            }
                        }
                    } catch (Exception e) {
                        log.error("数据读取失败:{}.", instId, e);
                    } finally {
                        bbPool.clear(buffer);
                    }
                }
                // 结束标识
                customer.getEnd().set(true);
            });
            return customer;
        }
        return LocalCustomer.empty();
    }

    @Override
    public List<String> getList(String instId, Bar bar) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");
        // 存在性判断
        if (!targetDir.exists()) {
            return Collections.EMPTY_LIST;
        }
        File[] files = targetDir.listFiles();
        if (files == null) {
            return Collections.EMPTY_LIST;
        }
        Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        List<String> res = new ArrayList<>();
        for (File file : files) {
            res.add(file.getName());
        }
        return res;
    }

    @Override
    public List<K> list(Long st, @Nullable Long et) {
        // TODO
        throw new TodoRuntimeException();
    }

    @Override
    public byte[] getByte(String instId, Bar bar, String date) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");
        // 存在性判断
        if (!targetDir.exists()) {
            return null;
        }
        File file = new File(targetDir, date);
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            log.error("读取文件失败:{}.", file.getAbsolutePath(), e);
            throw new TodoRuntimeException();
        }
    }

    @Override
    public void saveFile(String instId, Bar bar, String date, byte[] bytes) {
        log.debug("数据存储. 产品: {}, 周期: {}, 日期: {}", instId, bar, date);
        String filePath = createFileName(instId, bar, date);
        // 判断 文件是否存在, 如果存在, 则删除
        File file = new File(filePath);
        if (file.exists()) {
            log.debug("删除旧文件:{}.", filePath);
            file.delete();
        }
        try {
            FileUtils.write(filePath, bytes);
        } catch (IOException e) {
            log.error("保存文件失败:{}.", filePath, e);
            throw new TodoRuntimeException();
        }
    }

//    @PostConstruct
//    public void init() {
//        checkFile("CRV-USDT-SWAP", Bar.M_1, "2024-12");
//    }

    @Override
    public void checkFile(String instId, Bar bar, String date) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");
        // 存在性判断
        if (!targetDir.exists()) {
            return;
        }
        File file = new File(targetDir, date);
        if (!file.exists()) {
            return;
        }

        K lastK = null;
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel();) {

            while (true) {
                List<K> ks = FileUtils.read(channel, bbPool.get());
                if (CollectionUtils.isEmpty(ks)) {
//                    log.error("文件内容为空:{}.", file.getAbsolutePath());
                    break;
                }
                for (K k : ks) {
                    // 检查时间
                    if (lastK == null) {
                        lastK = k;
                        continue;
                    }
                    if (lastK.getTs() + bar.getScale() != k.getTs()) {
                        log.error("文件内容错误.\n last:\n{}. now: \n{}", lastK, k);
                        return;
                    }
                    lastK = k;
                }
            }

            log.info("文件校验通过:{}.", file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteLastMonth(String instId, Bar bar) {
        File targetDir = new File(STR."\{kStoreConfig.getStoreRoot()}\{instId}/\{bar.getValue()}");
        // 存在性判断
        if (!targetDir.exists()) {
            return true;
        }
        File[] files = targetDir.listFiles();
        if (files == null || files.length == 0) {
            return true;
        }
        Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        // 删除最后一个
        files[files.length - 1].delete();
        return true;
    }

    private String createFileName(String instId, Bar bar, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(kStoreConfig.getStoreRoot())
                .append(instId).append(File.separator)
                .append(bar.getValue()).append(File.separator);
        sb.append(date);
        String filePath = sb.toString();
        FileUtils.checkAndCreateParentPath(filePath);
        return sb.toString();
    }

    private String createFileName(String instId, Bar bar, LocalDate localDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(kStoreConfig.getStoreRoot())
                .append(instId).append(File.separator)
                .append(bar.getValue()).append(File.separator);
        String month;
        int monthValue = localDate.getMonthValue();
        if (monthValue < 10) {
            month = "0" + monthValue;
        } else {
            month = String.valueOf(monthValue);
        }
        sb.append(localDate.getYear()).append("-").append(month);
        String filePath = sb.toString();
        FileUtils.checkAndCreateParentPath(filePath);
        return filePath;
    }

    @Override
    public InstStoreInfo getInst(String instId) {
        String ccy = InstUtils.getBase(instId);
        List<InstStoreInfo> instList = getInstList();
        for (InstStoreInfo instStoreInfo : instList) {
            if (instStoreInfo.getCcy().equals(ccy)) {
                return instStoreInfo;
            }
        }
        return null;
    }

    @Override
    public List<InstStoreInfo> getInstList() {
        // 获取根目录
        File rootFile = new File(STR."\{kStoreConfig.getStoreRoot()}");
        return FileUtils.buildInstStoreInfoList(rootFile);
    }


}
