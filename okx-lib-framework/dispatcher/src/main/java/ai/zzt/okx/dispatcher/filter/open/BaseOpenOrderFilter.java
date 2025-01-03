package ai.zzt.okx.dispatcher.filter.open;

import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.dispatcher.engine.impl.vo.PlaceOrderT;
import ai.zzt.okx.dispatcher.request.OrderRequest;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.OrderService;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开仓过滤器
 *
 * @author zhouzhitong
 * @since 2024-08-29
 **/
@Slf4j
public abstract class BaseOpenOrderFilter implements OpenOrderFilter {

    @Resource
    private OrderService orderService;

    /**
     * 下单冷静期
     */
    protected final Map<String, PlaceOrderT> placeOrderTMap = new ConcurrentHashMap<>();

    /**
     * 最近一次开仓时间
     */
    protected final Map<String, Long> lastOpenTimeMap = new ConcurrentHashMap<>();

    @Override
    public int order() {
        return Ordered.HIGH;
    }

    @Override
    public boolean doFilter(TradeOrderRequest request) {
        PlaceOrderSettings settings = request.getSettingsContext().getPlaceOrderSettings();
//        if (settings.getLimit().getType() != type()) {
//            return false;
//        }
        TrendType analyzeTrend = request.getAnalyzeContext().getAnalyzeTrend();

        String instId = request.getInstId();
        PositionContext positionContext = request.getAccountContext().getPositionContext();
        PositionFace positionWrapper = positionContext.getFace(instId);

        PositionsSide opSide = null;
        Positions positions = null;

        if (analyzeTrend == TrendType.UP) {
            opSide = PositionsSide.LONG;
            positions = positionWrapper.shortPositions.positions;
        } else if (analyzeTrend == TrendType.DOWN) {
            opSide = PositionsSide.SHORT;
            positions = positionWrapper.longPositions.positions;
        } else {
            return false;
        }

        if (positionWrapper.isEmpty()) {
            // 没有持仓, 开多
//            log.debug("当前没有持仓, 准备开仓: {}", instId);
            openPositions(request, opSide);
            return true;
        } else {
            Positions pendingPositions = positionWrapper.pendingPositions;
            // 开仓条件: 符合下单配置  & 不能存在还没同步的仓库订单
            if (positions == null) {
                // TODO 这里缺少当前仓位是否盈利的判断, 如果盈利了就不要加仓了
                boolean canOpenPositions = request.canOpenPositions() && pendingPositions == null;
                if (canOpenPositions) {
                    // 没有持多仓, 且能够开仓
                    openPositions(request, opSide);
                }
            }
        }
        return false;
    }

    /**
     * 开仓. 到这里已经是符合开仓的(配置)条件了, 后面的逻辑就不需要考虑到配置因素了.
     * <p>
     * TODO 开仓前应该判断当前账户余额是否足够
     *
     * @param request 下单请求
     * @param posSide 持仓方向
     */
    protected final void openPositions(TradeOrderRequest request, PositionsSide posSide) {
        String instId = request.getInstId();
        PlaceOrderSettings settings = request.getSettingsContext().getPlaceOrderSettings();
        Set<PositionsSide> sides = settings.getLimit().getSides();
        if (!sides.contains(posSide)) {
            // 不支持开仓方向
            return;
        }
        if (lastOpenTimeMap.containsKey(instId)) {
            Long currentTs = request.getMarkPrice().getTs();
            Long lastOpenTime = lastOpenTimeMap.get(instId);
            if (lastOpenTime > currentTs) {
                // 针对模拟器的处理
                // TODO 模拟器处理, 会把其他任务延迟也处理掉。
                lastOpenTimeMap.remove(instId);
            } else if (currentTs - lastOpenTime < settings.getLimit().getOpenIntervalTime()) {
                return;
            }
        }

        BigDecimal markPx = request.getMarkPrice().getMarkPx();
        if (posSide == PositionsSide.LONG) {
            if (settings.getLimit().getMaxPrice() != null && settings.getLimit().getMaxPrice().compareTo(markPx) <= 0) {
                log.trace("当前价格大于最大开仓价格, 不开多仓");
                return;
            }
        } else {
            if (settings.getLimit().getMinPrice() != null && settings.getLimit().getMinPrice().compareTo(markPx) >= 0) {
                log.trace("当前价格小于最小开仓价格, 不开空仓");
                return;
            }
        }

        if (!check(request.getMarkPrice(), posSide, settings.getLimit().getCoolingOffTime())) {
            return;
        }

        OrderRequest orderRequest = request.getOrderRequest();
        orderRequest.setPositionsSide(posSide);

        PositionFace wrapper = request.getAccountContext().getPositionContext().getFace(instId);
        if (orderService.placeOrder(request)) {
            wrapper.setPendPositions(instId);
            lastOpenTimeMap.put(instId, request.getMarkPrice().getTs());
        }
    }

    protected abstract InstrumentType type();

    /**
     * 判断是否超过冷静期
     *
     * @param mp      市场价格
     * @param posSide 趋势类型
     * @return true
     */
    private boolean check(MarkPrice mp, PositionsSide posSide, long ts) {
        if (posSide == null) {
            return false;
        }
        if (ts == 0) {
            return true;
        }
        String instId = mp.getInstId();
        PlaceOrderT placeOrderT = placeOrderTMap.get(instId);
        if (placeOrderT == null) {
            placeOrderT = new PlaceOrderT(instId, posSide, mp.getTs());
            placeOrderTMap.put(instId, placeOrderT);
            return false;
        }
        if (placeOrderT.getPosSide() == posSide) {
            // 判断是否超过冷静期 20s
            return mp.getTs() - placeOrderT.getStartTime() > ts;
        } else {
            placeOrderT.setPosSide(posSide);
            placeOrderT.setStartTime(mp.getTs());
        }
        return false;
    }


}
