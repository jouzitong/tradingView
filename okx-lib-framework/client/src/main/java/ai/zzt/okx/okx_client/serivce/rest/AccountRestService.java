package ai.zzt.okx.okx_client.serivce.rest;

import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.serivce.IAccountService;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.AccountApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.rest.account.req.LeverageReq;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;
import io.reactivex.rxjava3.core.Single;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账号 API service
 *
 * @author zhouzhitong
 * @since 2024/10/23
 **/
@Service
@Slf4j
public class AccountRestService implements IAccountService {

    @Resource
    private AccountApi accountApi;

    @Resource
    private AccountContext accountContext;

    /**
     * 同步杠杆（全仓 + 合约）
     *
     * @param instId 产品ID
     * @param lever  目标杠杆
     * @return 最终杠杆
     */
    @Override
    public BigDecimal syncLever(String instId, @Nullable BigDecimal lever) {
        return doSyncLever(InstrumentContext.get(instId, InstrumentType.SWAP), lever);
//        doSyncLever(InstrumentContext.get(instId, InstrumentType.MARGIN), lever);
    }

    @Override
    public boolean syncAccount() {
        log.debug("同步账户信息（购买的现货）");
        R<Balance> res = OkxRestApiFactory.get(accountApi.getBalance(null));
        if (res.isOk()) {
            for (Balance balance : res.getData()) {
                accountContext.updateBalance(balance);
            }
        }
        return true;
    }

    @Override
    public boolean syncPosition() {
        log.debug("同步仓位信息");
        int count = 0;
        while (true) {
            try {
                R<Positions> positions = OkxRestApiFactory.get(accountApi.getPositions());
                PositionContext positionContext = accountContext.getPositionContext();
                positionContext.addAll(positions.getData());
                return true;
            } catch (Exception e) {
                log.error("获取持仓失败[{}].", count, e);
                if (count++ > 5) {
                    return false;
                }
                ThreadUtils.sleep(1);
            }
        }
    }

    @Override
    public boolean syncPendingOrders() {
        log.debug("同步 Algo Order 数据");
        for (OrdType value : OrdType.values()) {
            int count = 0;
            while (true) {
                try {
                    R<AlgoOrder> res = OkxRestApiFactory.get(
                            accountApi.getPendingAlgoOrders(value.value(), InstrumentType.SWAP.getValue()));
                    if (res.isOk()) {
                        List<AlgoOrder> aos = res.getData();
                        OrderContext orderContext = accountContext.getOrderContext();
                        orderContext.addAlgoOrders(aos);
                    }
                    break;
                } catch (Exception e) {
                    if (count++ > 5) {
                        return false;
                    }
                    log.error("type: {}-{} 获取委托单失败: {}", value, count, e.getMessage());
                    ThreadUtils.sleep(1);
                }
            }
        }
        return true;
    }

    /**
     * 同步杠杆
     *
     * @param instruments 产品ID
     * @param lever       目标杠杆
     * @return 最终杠杆数
     */
    protected BigDecimal doSyncLever(Instruments instruments, BigDecimal lever) {
        if (instruments == null) {
            return null;
        }
        String instId = instruments.getInstId();
        if (lever != null && lever.compareTo(instruments.getLever()) > 0) {
            log.warn("{} 杠杆倍数超过了最大值 {}", instId, instruments.getLever());
            lever = instruments.getLever();
        }
        // TODO 目前只通过全仓的杠杆
        Single<R<LeverageResp>> single = accountApi.getLeverageInfo(instId, null, MgnMode.CROSS.getValue());
        R<LeverageResp> res = OkxRestApiFactory.get(single);
        LeverageResp resp = res.getData().getFirst();
        if (lever == null) {
            if (res.isOk()) {
                for (LeverageResp re : res.getData()) {
                    accountContext.updateLever(re);
                }
            } else {
                log.error("获取杠杆异常: {}", JackJsonUtils.toStr(res));
            }
            return resp.getLever();
        }
        if (resp.getLever().compareTo(lever) == 0) {
            log.debug("{} 杠杆倍数已经是目标值 {}", instId, lever);
        } else {
            log.info("{} 杠杆倍数从 {} 修改为 {}", instId, resp.getLever(), lever);
            LeverageReq req = new LeverageReq();
            req.setInstId(instId);
            req.setLever(lever);
            req.setMgnMode(MgnMode.CROSS);
            single = accountApi.setLeverage(req);
            res = OkxRestApiFactory.get(single);
            if (res.isOk()) {
                for (LeverageResp re : res.getData()) {
                    accountContext.updateLever(re);
                }
            }
        }
        return resp.getLever();
    }

}
