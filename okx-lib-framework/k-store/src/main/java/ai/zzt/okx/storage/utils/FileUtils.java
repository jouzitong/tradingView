package ai.zzt.okx.storage.utils;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.vo.TimeRange;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/6/23
 **/
@Slf4j
public class FileUtils {

    public static void write(String path, ByteBuffer buffer) throws IOException {
        try (FileChannel channel = new FileOutputStream(path, true).getChannel()) {
            channel.write(buffer);
        }
    }

    public static void write(String path, byte[] bytes) throws IOException {
        try (FileChannel channel = new FileOutputStream(path, true).getChannel()) {
            channel.write(ByteBuffer.wrap(bytes));
        }
    }

    public static List<K> read(FileChannel channel, ByteBuffer buffer) throws IOException {
        try {
            if (channel.read(buffer) == -1) {
                return null;
            }
            buffer.flip();
            List<K> res = new ArrayList<>();
            int position = buffer.limit();
            int size = bufferSize(K.class);
            int count = position / size;
            for (int j = 0; j < count; j++) {
                K k = new K();
                k.setTs(buffer.getLong());
                k.setO(BigDecimal.valueOf(buffer.getDouble()));
                k.setH(BigDecimal.valueOf(buffer.getDouble()));
                k.setL(BigDecimal.valueOf(buffer.getDouble()));
                k.setC(BigDecimal.valueOf(buffer.getDouble()));
                k.setConfirm(buffer.getLong());
                res.add(k);
            }
            return res;
        } finally {
            buffer.clear();
        }
    }

    public static int bufferSize(Class<?> c) {
        if (c == K.class) {
            return (Long.BYTES * 2 + Double.BYTES * 4);
        }
        throw new TodoRuntimeException("未定义");
    }

    public static void write(@Nullable String path, K k, ByteBuffer buffer) {
        try {
            buffer.putLong(k.getTs());
            buffer.putDouble(k.getO().doubleValue());
            buffer.putDouble(k.getH().doubleValue());
            buffer.putDouble(k.getL().doubleValue());
            buffer.putDouble(k.getC().doubleValue());
            buffer.putLong(k.getConfirm());
        } catch (Exception e) {
            log.error("write error", e);
        }
        if (path == null) {
            return;
        }
        buffer.flip();
        try {
            write(path, buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkAndCreateParentPath(String filePath) {
        File file = new File(filePath);

        // 确保父文件夹存在
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            boolean createDir = parentDir.mkdirs();
            if (!createDir) {
                throw new RuntimeException(STR."文件层级创建失败:\{file.getAbsolutePath()}");
            }
        }
    }

    public static List<MarkPrice> convert(String instId, ByteBuffer buffer) {
        try {
            int position = buffer.limit();
            int size = (Long.BYTES + Double.BYTES); // MarkPrice 存储大小
            int count = position / size;
            List<MarkPrice> mps = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                MarkPrice markPrice = ObjectsPool.get(MarkPrice.class);
                markPrice.setInstId(instId);
                markPrice.setTs(buffer.getLong());
                markPrice.setMarkPx(BigDecimal.valueOf(buffer.getDouble()));
                mps.add(markPrice);
            }
            return mps;
        } catch (Exception e) {
            log.error("convert error", e);
        } finally {
            buffer.clear();
            buffer.flip();
            buffer.compact();
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 遍历目录结构并封装为 InstStoreInfo 列表
     *
     * @param rootDir 根目录
     * @return 封装后的 InstStoreInfo 列表
     */
    public static List<InstStoreInfo> buildInstStoreInfoList(File rootDir) {
        List<InstStoreInfo> instStoreInfoList = new ArrayList<>();

        // 遍历产品ID目录 (BTC, ETH, XXX)
        File[] productDirs = rootDir.listFiles();
        if (productDirs != null) {
            for (File productDir : productDirs) {
                if (productDir.isDirectory()) {
                    InstStoreInfo info = new InstStoreInfo();
                    info.setInstId(productDir.getName());
                    Map<Bar, TimeRange> barsDateMap = new HashMap<>();
                    Map<Bar, List<String>> fileBarMap = new HashMap<>();

                    // 遍历时间粒度目录 (1s, 1m)
                    File[] barDirs = productDir.listFiles();
                    if (barDirs != null) {
                        for (File barDir : barDirs) {
                            if (barDir.isDirectory()) {
                                Bar bar = Bar.ofVal(barDir.getName());
                                // 遍历时间区间目录 (2021-01, 2022-02)
                                File[] dateDirs = barDir.listFiles();
                                if (dateDirs != null) {
                                    List<String> dates = new ArrayList<>();
                                    for (File dateDir : dateDirs) {
                                        if (dateDir.isFile()) {
                                            dates.add(dateDir.getName());
                                        }
                                    }
                                    fileBarMap.put(bar, dates);
                                    // 创建 DateInterval 并存入 Map
                                    TimeRange dateInterval = buildDateInterval(dates);
                                    barsDateMap.put(bar, dateInterval);
                                }
                            }
                        }
                    }
                    info.setBarsDateMap(barsDateMap);
                    info.setFileBarMap(fileBarMap);
                    instStoreInfoList.add(info);
                }
            }
        }
        return instStoreInfoList;
    }

    /**
     * 根据日期列表构建 DateInterval
     *
     * @param dates 日期列表
     * @return 日期区间对象
     */
    public static TimeRange buildDateInterval(List<String> dates) {
        if (dates.isEmpty()) {
            return null;
        }

        // 假设日期目录格式为 "yyyy-MM"，需要排序取最小和最大值
        dates.sort(String::compareTo);
        String startDate = dates.get(0);
        String endDate = dates.get(dates.size() - 1);
        return new TimeRange(startDate, endDate);
    }

}
