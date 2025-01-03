package ai.zzt.okx.storage.engine;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.storage.engine.param.ScrollParams;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;

import java.math.BigDecimal;
import java.util.List;

/**
 * K 线数据引擎管理
 *
 * @author zhouzhitong
 * @since 2024/11/2
 **/
public interface IKLineEngine {

    /**
     * 添加标记价格
     *
     * @param mp 添加标记价格
     */
    void addMp(MarkPrice mp);

    /**
     * 获取区间当前最大差价比率
     *
     * @param instId 产品ID
     * @param period 周期内
     * @return 最大差价比率
     */
    BigDecimal getMaxDiffRate(String instId, long period);

    /**
     * 获取K线数据列表
     *
     * @param params 滚动查询参数
     * @return K线数据列表
     */
    List<K> getKLines(ScrollParams params);

    /**
     * 获取最新的K线数据
     *
     * @param id  产品ID
     * @param bar 周期
     * @return 最新的K线数据
     */
    K getLastK(String id, Bar bar);

    /**
     * 刷新数据到磁盘中
     */
    void flush();

}
