package ai.zzt.okx.dispatcher.request;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.calculate.context.StatisticsContext;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Data
public class TradeOrderRequest implements Serializable {

    /**
     * 会话ID(emulator 中的taskId)
     */
    @JsonIgnore
    private String sessionId;

    /**
     * 交易所名称
     */
    private String instId;

    /**
     * 市场价格
     */
    private MarkPrice markPrice;

    /**
     * 当前手续费率.
     * <p>
     * TODO 目前没怎么用到, 后续可以考虑去掉
     */
    @Deprecated
    @JsonIgnore
    private FundingRate fundingRate;

    /**
     * 配置上下文
     */
    private SettingsContext settingsContext;

    /**
     * 账户上下文
     */
    private AccountContext accountContext;

    /**
     * 分析上下文
     */
    @JsonIgnore
    private AnalyzeContext analyzeContext;

    /**
     * 统计上下文. 这个不是必要的, 可以选择性的使用.
     */
    @Nullable
    private StatisticsContext statisticsContext;

    // ================================

    /**
     * 下单请求
     */
    private OrderRequest request;

    /**
     * 【模拟任务需要】超时持仓.
     */
    private boolean overTime;


    public TradeOrderRequest() {
    }

    public TradeOrderRequest(MarkPrice markPrice) {
        this.markPrice = markPrice;
        this.instId = markPrice.getInstId();
    }

    public InstrumentType instType() {
        return markPrice.getInstType();
    }

    public AnalyzeContext getAnalyzeContext() {
        if (analyzeContext == null) {
            analyzeContext = new AnalyzeContext(getInstId(), settingsContext);
        }
        return analyzeContext;
    }

    public OrderRequest getOrderRequest() {
        if (request == null) {
            request = new OrderRequest(getInstId());
        }
        return request;
    }

    public List<AlgoOrder> getAlgoOrders() {
        return accountContext.getOrderContext().getAlgoOrders(this.getInstId());
    }

    public FundingRate getFundingRate() {
        if (fundingRate == null) {
            fundingRate = new FundingRate();
            fundingRate.setFundingRate(BigDecimal.valueOf(0.0007));
        }
        return fundingRate;
    }

    /**
     * 判断能开仓
     * <pre>
     *  判断条件:
     *  1. 开仓数量是否达到最大值
     *  2. 距离上次开仓时间间隔是否满足
     * </pre>
     *
     * @return true 能开仓
     */
    public boolean canOpenPositions() {
        PositionContext positionContext = accountContext.getPositionContext();
        PositionFace positionWrapper = positionContext.getFace(this.instId);

        PlaceOrderSettings settings = settingsContext.getPlaceOrderSettings();

        long lastOpenTime = positionWrapper.lastOpenTime;
        long minIntervalTime = settings.getLimit().getMinIntervalTime();

        int openCount = positionWrapper.getOpenCount();
        int maxCount = settings.getLimit().getMaxCount();
        return openCount < maxCount && lastOpenTime + minIntervalTime < markPrice.getTs();
    }

    public void clear() {
        this.markPrice = null;
        this.fundingRate = null;
        if (request != null) {
            this.request.clear();
        }
        if (analyzeContext != null) {
            this.analyzeContext.clear();
        }
    }

    public List<Positions> getPositions(String instId) {
        return accountContext.getPositionContext().getPosition(instId);
    }

    public List<PositionsWrapper> getPositionsWrapper(String instId) {
        return accountContext.getPositionContext().getFace(instId).getWrappers();
    }


}
