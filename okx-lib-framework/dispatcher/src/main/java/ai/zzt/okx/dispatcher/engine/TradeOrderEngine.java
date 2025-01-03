package ai.zzt.okx.dispatcher.engine;

import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
public interface TradeOrderEngine {

    /**
     * 初始化
     *
     * @param instId 合约ID
     */

    void init(String instId);

    /**
     * 删除正在计算的币
     *
     * @param instId 合约ID
     */
    void delete(String instId);

    /**
     * 调度
     *
     * @param mps 市场数据列表
     */
    void dispatch(List<MarkPrice> mps);

    /**
     * 调度
     *
     * @param mp 市场数据
     */
    void dispatch(MarkPrice mp);

}
