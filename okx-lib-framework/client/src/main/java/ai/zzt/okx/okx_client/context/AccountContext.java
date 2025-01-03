package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.okx_client.context.bean.InstrumentsLever;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/5/5
 **/
@Component
@Data
@Slf4j
public class AccountContext implements IAccountContext {

    /**
     * 账户余额信息、现货信息. 例如稳定货币 USDT. 常见的交易货币 BTC、ETH等
     */
    private Balance balance;

    /**
     * 仓位上下文
     */
    private PositionContext positionContext = new PositionContext();

    /**
     * 订单上下文
     */
    @JsonIgnore // TODO 临时不序列化, 后续需要补上
    private OrderContext orderContext = new OrderContext();

    /**
     * 产品杠杆信息, 主要针对合约、杠杆交易
     * <p>
     * key: 产品ID
     * value-key: 产品类型
     * value: 杠杆信息
     */
    private Map<String, Map<InstrumentType, InstrumentsLever>> instLeverMap = new ConcurrentHashMap<>();

    /**
     * 更新账户上下文信息
     */
    public void updateBalance(Balance balance) {
        this.balance = balance;
    }

    /**
     * 更新杠杆信息
     *
     * @param resp 杠杆响应体
     */
    public void updateLever(LeverageResp resp) {
        String instId = InstUtils.getBase(resp.getInstId());
        Map<InstrumentType, InstrumentsLever> ilMap = instLeverMap.computeIfAbsent(instId, _ -> new ConcurrentHashMap<>());
        InstrumentType type = InstUtils.getType(instId);
        InstrumentsLever instrumentsLever = ilMap.computeIfAbsent(type, _ -> new InstrumentsLever(instId, type));
        instrumentsLever.update(resp);
    }

    @Nullable
    public BigDecimal getLever(String instId, InstrumentType type, PositionsSide posSide, MgnMode mode) {
        if (type == InstrumentType.SPOT) {
            return BigDecimal.ONE;
        }
        instId = InstUtils.getBase(instId);
        Map<InstrumentType, InstrumentsLever> ilMap = instLeverMap.get(instId);
        if (ilMap == null) {
            return null;
        }
        InstrumentsLever instrumentsLever = ilMap.get(type);
        return instrumentsLever.getLever(mode, posSide);
    }

    @Override
    public PositionContext getPositionContext() {
        return positionContext;
    }

    @Override
    public OrderContext getOrderContext() {
        return orderContext;
    }
}
