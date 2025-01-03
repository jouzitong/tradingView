package ai.zzt.okx.v5.ws.pri;

import ai.zzt.okx.v5.entity.ws.request.pri.AccountArg;
import ai.zzt.okx.v5.entity.ws.request.pri.AccountGreeksArg;
import ai.zzt.okx.v5.entity.ws.request.pri.BalanceAndPositionArg;
import ai.zzt.okx.v5.entity.ws.request.pri.LiquidationWarningArg;
import ai.zzt.okx.v5.entity.ws.request.pri.LoginArg;
import ai.zzt.okx.v5.entity.ws.request.pri.PositionsArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.WsApi;

/**
 * TODO <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket">交易账户 ws api</a>
 *
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public interface AccountWsApi extends WsApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket-login">登录</a>
     *
     * @param loginArg  登录参数
     * @param secretKey 私钥
     */
    void login(LoginArg loginArg, String secretKey);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket-account-channel">账户信息</a>
     *
     * @param accountArg 账户参数
     */
    default void account(AccountArg accountArg) {
        account(accountArg, Operation.SUBSCRIBE);
    }

    default void account(AccountArg accountArg, Operation op) {
        operate(op, WsChannel.PRIVATE, accountArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket-balance-channel">持仓频道</a>
     *
     * @param positionsArg 仓位参数
     */
    default void positions(PositionsArg positionsArg) {
        positions(positionsArg, Operation.SUBSCRIBE);
    }

    default void positions(PositionsArg positionsArg, Operation op) {
        operate(op, WsChannel.PRIVATE, positionsArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket-balance-and-positions-channel">账户余额和持仓</a>
     *
     * @param arg 账户余额和持仓参数
     */
    default void balanceAndPositions(BalanceAndPositionArg arg) {
        balanceAndPositions(arg, Operation.SUBSCRIBE);
    }

    default void balanceAndPositions(BalanceAndPositionArg arg, Operation op) {
        operate(op, WsChannel.PRIVATE, arg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-websocket-position-risk-warning">爆仓风险预警推送频道</a>
     *
     * @param liquidationWarningArg 账户参数
     */
    default void liquidationOrders(LiquidationWarningArg liquidationWarningArg) {
        liquidationOrders(liquidationWarningArg, Operation.SUBSCRIBE);
    }

    default void liquidationOrders(LiquidationWarningArg liquidationWarningArg, Operation op) {
        operate(op, WsChannel.PRIVATE, liquidationWarningArg);
    }

    default void accountGreeks(AccountGreeksArg arg) {
        accountGreeks(arg, Operation.SUBSCRIBE);
    }

    default void accountGreeks(AccountGreeksArg arg, Operation op) {
        operate(op, WsChannel.PRIVATE, arg);
    }

}
