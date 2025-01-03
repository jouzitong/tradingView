package ai.zzt.okx.tradeOrder.serivce;

import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.utils.SwapCoinUtils;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.dispatcher.request.OrderRequest;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.OrderService;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.OrderApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.CloseOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.PlaceOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.PlaceOrderRsp;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 创建orderService,下单之后止盈止损
 * <p>
 * 合约 + 全仓
 *
 * @author liuxioakun
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OkxSwapOrderService implements OrderService {

    /**
     * 下单的api
     */
    private final OrderApi orderApi;

    private final AppProperties appProperties;

    private final OrderAlgoService orderAlgoService;

    private final AccountContext accountContext;

    @Override
    public boolean placeOrder(TradeOrderRequest request) {
        PlaceOrderSettings settings = request.getSettingsContext().getPlaceOrderSettings();
        // 是否开启自动下单
        if (!settings.isEnablePlaceOrder()) {
            log.trace("【自动下单】{} 已关闭.", request.getInstId());
            return false;
        }

        OrderRequest orderRequest = request.getOrderRequest();
        MarkPrice mp = request.getMarkPrice();

        String instId = request.getInstId();
        BigDecimal price = mp.getMarkPx();
        PositionsSide side = orderRequest.getPositionsSide();

//        BigDecimal lever = InstrumentContext.getLever(instId, InstrumentType.SWAP, orderRequest.getPositionsSide(), MgnMode.CROSS);
        BigDecimal lever = settings.getLever();

        // 下单金额
        BigDecimal cash = settings.getCash();

        log.info("【下单-开仓】产品: {} , 开仓价: {}, 方向: {}, 预计下单金额: {}", instId, price, side, cash);

        PlaceOrderReq placeOrderReq = buildOpenRequest(request.getMarkPrice(), side, cash, lever);
        Single<R<PlaceOrderRsp>> rSingle = orderApi.placeOrder(placeOrderReq);
        try {
            R<PlaceOrderRsp> execute = OkxRestApiFactory.get(rSingle);
            log.info("【下单-开仓】结果: {}. 返回数据: {}", execute.isOk(), execute.getData());
            if (execute.isOk()) {
                // 下单成功, 计数
                return true;
            } else {
                log.warn("【下单-开仓】失败: {}", execute.getMsg());
            }
        } catch (Exception e) {
            log.error("【下单-开仓】异常: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean closeOrder(TradeOrderRequest request) {
        String instId = request.getInstId();
        OrderRequest orderRequest = request.getOrderRequest();

        // 如果ordType != null, 说明是一个策略下单逻辑
        if (orderRequest.getOrdType() != null) {
            return orderAlgoService.closeOrder(request);
        }

        PositionsSide positionsSide = orderRequest.getPositionsSide();

        Positions positions = request.getAccountContext().getPositionContext().getFace(instId).get(positionsSide);
        if (positions == null) {
            log.warn("【平仓-失败】无持仓");
            return false;
        }

        log.info("【市价平仓】产品: {}", instId);
        CloseOrderReq closeOrderReq = CloseOrderReq.builder()
                .instId(instId)
                .posSide(positions.getPosSide())
                .mgnMode(positions.getMgnMode()).build();
        try {
            R<PlaceOrderRsp> r = OkxRestApiFactory.get(orderApi.closePosition(closeOrderReq));
            log.info("【市价平仓】结果: {}. 返回数据: {}", r.getCode(), r.getMsg());
            // FIXME 这个 code 处理, 应该是由拦截器处理
            if (51023 == r.getCode()) {
                log.info("【市价平仓】无持仓");
                PositionContext positionContext = accountContext.getPositionContext();
                positionContext.remove(instId, positions.getPosSide());
                return false;
            }
            if (r.isOk()) {
                return true;
            } else {
                log.warn("【市价平仓】失败: {}", r);
            }
        } catch (Exception e) {
            log.error("【市价平仓】异常: {}", e.getMessage());
        }
        return false;
    }

    // TODO ====================== 测试代码, 需要迁移到测试目录下 ======================

    //    @PostConstruct
    public void init() {
        ThreadUtils.runIO(() -> {
            if (!appProperties.isSimulated()) {
                throw new RuntimeException("正式环境不允许启动该代码");
            }
            ThreadUtils.sleep(10);
            MarkPrice mp = new MarkPrice();
            mp.setInstId("BTC-USDT-SWAP");
            mp.setMarkPx(new BigDecimal("68000"));
            PlaceOrderReq req = buildOpenRequest(mp, PositionsSide.LONG, BigDecimal.valueOf(600), BigDecimal.valueOf(100));
            Single<R<PlaceOrderRsp>> orderSingle = orderApi.placeOrder(req);
            R<PlaceOrderRsp> orderExecute = OkxRestApiFactory.get(orderSingle);
            System.out.println(orderExecute);
//        CloseOrderReq closeOrderReq = CloseOrderReq.builder().instId("CRV-USDT").ccy("USDT").mgnMode(MgnMode.CROSS).build();
//        Single<R<PlaceOrderRsp>> rSingle = orderApi.closePosition(closeOrderReq);
//        R<PlaceOrderRsp> execute = OkxRestApiFactory.get(rSingle);
//        System.out.println(execute);
        });
    }

    private PlaceOrderReq buildOpenRequest(MarkPrice mp, PositionsSide side, BigDecimal cash, BigDecimal lever) {
        String instId = mp.getInstId();
        BigDecimal px = mp.getMarkPx();
        PlaceOrderReq req = new PlaceOrderReq();
        req.setInstId(instId);
        req.setTdMode(TdMode.CROSS); //全仓
        if (side == PositionsSide.SHORT) {
            req.setSide(Side.SELL);
        } else {
            req.setSide(Side.BUY);
        }
        req.setOrdType(OrderType.MARKET);//市场价
        req.setCcy("USDT"); //
        req.setSz(SwapCoinUtils.coin(instId, cash, px, lever));
        if (req.getSz().compareTo(BigDecimal.ZERO) == 0) {
            req.setSz(new BigDecimal("1"));
        }
        req.setPosSide(side);
        return req;
    }

    @Override
    public InstrumentType type() {
        return InstrumentType.SWAP;
    }
}
