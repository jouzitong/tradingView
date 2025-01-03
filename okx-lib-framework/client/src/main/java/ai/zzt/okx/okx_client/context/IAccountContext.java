package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.math.BigDecimal;

/**
 * 该接口应参考欧易的接口<a href="https://www.okx.com/docs-v5/zh/?shell#trading-account">参考地址</a>
 *
 * @author zhouzhitong
 * @since 2024/11/1
 **/
public interface IAccountContext {

    /**
     * 更新账户信息
     *
     * @param balance 账户信息
     */
    void setBalance(Balance balance);

    /**
     * 获取账户信息
     *
     * @return 账户信息
     */
    Balance getBalance();

    /**
     * 获取订单上下文对象
     *
     * @return 订单上下文对象
     */
    OrderContext getOrderContext();

    /**
     * 获取仓位上下文对象
     *
     * @return 仓位上下文对象
     */
    PositionContext getPositionContext();

    /**
     * 获取杠杆信息
     *
     * @param instId  产品
     * @param type    产品类型
     * @param posSide 仓位方向
     * @param mode    模式
     * @return 杠杆倍数
     */
    BigDecimal getLever(String instId, InstrumentType type, PositionsSide posSide, MgnMode mode);

    /**
     * 更新杠杆信息
     *
     * @param resp 杠杆响应体
     */
    void updateLever(LeverageResp resp);

}
