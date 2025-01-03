package ai.zzt.okx.storage.service;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.web.dto.InstStoreInfo;
import ai.zzt.okx.storage.bean.Customer;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author wyz
 */
public interface KStoreService {

    /**
     * 数据存储
     *
     * @param instId 币种
     * @param bar    时间周期
     * @param k      K线数据
     */
    void store(String instId, Bar bar, K k);

    default void store(String instId, Bar bar, List<K> ks) {
        for (K k : ks) {
            store(instId, bar, k);
        }
    }

    /**
     * 创建消费者
     *
     * @param bar    时间粒度
     * @param instId 产品Id
     * @return 消费者
     */
    Customer<MarkPrice> createCustomer(Bar bar, String instId);

    /**
     * 创建消费者
     *
     * @param bar    时间粒度
     * @param instId 产品Id
     * @param st     开始时间
     * @return 消费者
     */
    Customer<MarkPrice> createCustomer(Bar bar, String instId, long st);

    /**
     * 获取收集了的月份数据列表
     *
     * @param instId 产品Id
     * @param bar    时间粒度
     * @return 月份数据列表
     */
    List<String> getList(String instId, Bar bar);

    /**
     * 查询历史数据, 最多 10_000 数据
     *
     * @param st 开始时间
     * @param et 结束时间. 如果为空, 则查询到最新数据
     * @return 历史K线数据
     */
    List<K> list(Long st, @Nullable Long et);

    /**
     * 删除上一个月的数据文件
     *
     * @param instId
     * @param bar
     * @return
     */
    boolean deleteLastMonth(String instId, Bar bar);

    /**
     * 获取存储产品信息列表
     *
     * @return 存储产品信息列表
     */
    List<InstStoreInfo> getInstList();

    InstStoreInfo getInst(String instId);

    /**
     * 获取文件字节
     *
     * @param instId 产品ID
     * @param bar    时间周期
     * @param date   日期. 格式: yyyy-MM
     * @return 文件字节
     */
    byte[] getByte(String instId, Bar bar, String date);

    /**
     * 保存文件
     *
     * @param instId 产品ID
     * @param bar    时间周期
     * @param date   日期. 格式: yyyy-MM
     * @param bytes  字节数据
     */
    void saveFile(String instId, Bar bar, String date, byte[] bytes);

    /**
     * 验证文件是否完整
     *
     * @param instId 产品ID
     * @param bar    时间周期
     * @param date   日期. 格式: yyyy-MM
     */
    void checkFile(String instId, Bar bar, String date);

}
