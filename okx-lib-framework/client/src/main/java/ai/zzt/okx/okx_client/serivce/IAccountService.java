package ai.zzt.okx.okx_client.serivce;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/10/30
 **/
public interface IAccountService {

    /**
     * 同步杠杆
     * <p>
     * lever = null 时, 不设置目标杠杆, 只获取
     *
     * @param instId 产品id
     * @param lever  杠杆倍数.
     * @return
     */
    BigDecimal syncLever(String instId, @Nullable BigDecimal lever);

    /**
     * 同步账户信息
     *
     * @return true:同步成功
     */
    boolean syncAccount();

    /**
     * 同步持仓信息
     *
     * @return true:同步成功
     */
    boolean syncPosition();

    /**
     * 同步挂单列表
     *
     * @return true:同步成功
     */
    boolean syncPendingOrders();

}
