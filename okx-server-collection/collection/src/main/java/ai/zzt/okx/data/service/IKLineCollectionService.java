package ai.zzt.okx.data.service;

import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.v5.enumeration.Bar;

/**
 * @author zhouzhitong
 * @since 2024/10/3
 **/
public interface IKLineCollectionService {

    /**
     * 收集所有未历史数据 (1M线数据)
     * <p>
     * 收集币种参考: {@link OkxProperties#getHotInstIdList()}
     */
    void collectHistoryData();

    /**
     * 收集所有未历史数据
     *
     * @param instId 币种
     * @param bar    时间粒度
     */
    void collectHistoryData(String instId, Bar bar);

}
