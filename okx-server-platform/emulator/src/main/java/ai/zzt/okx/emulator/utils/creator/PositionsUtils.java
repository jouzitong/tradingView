package ai.zzt.okx.emulator.utils.creator;

import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.math.BigDecimal;

/**
 * 模拟器 --- 仓位工具类
 * <p>
 * 主要是将下单请求转换为仿真器的仓位对象
 *
 * @author zhouzhitong
 * @since 2024/6/26
 **/
public class PositionsUtils {


    public static PositionContext buildPositionContext() {
        PositionContext positionContext = new PositionContext();
        positionContext.setLastUpdateTime(System.currentTimeMillis());
        return positionContext;
    }

    /**
     * 下单
     *
     * @param order 委托单
     * @return 仓位对象
     */
    public static Positions open(Order order, MarkPrice mp) {
        Positions positions = newPosition(mp);
        positions.setLever(BigDecimal.valueOf(3));
        positions.setMarkPx(mp.getMarkPx());
        positions.setPosSide(order.getPosSide());
        positions.setLast(mp.getMarkPx());
        positions.setTradeId(order.getOrdId());

        //开仓平均价格
        BigDecimal avgPx = positions.getAvgPx();

        //持仓数量
        positions.setPos(positions.getPos().add(order.getSz()));
        positions.setAvgPx(avgPx);

        //可用仓位数量
        BigDecimal availPos = positions.getAvailPos();
        positions.setAvailPos(availPos.add(order.getSz()));

        return positions;
    }

    /**
     * 加仓
     *
     * @param positions 仓位
     * @param order     委托单
     * @param mp        当前价格
     */
    public static void add(Positions positions, Order order, MarkPrice mp) {

    }

    public static Positions newPosition(MarkPrice markPrice) {
        Positions positions = new Positions();
        positions.setInstId(markPrice.getInstId());
        positions.setInstType(markPrice.getInstType() != null ? markPrice.getInstType() : InstrumentType.SWAP);
        positions.setAdl(0);
        positions.setAvailPos(BigDecimal.ZERO);
        positions.setCcy("USDT");
        positions.setAvgPx(BigDecimal.ZERO);
        positions.setBePx(BigDecimal.ZERO);
        positions.setFee(BigDecimal.ZERO);
        positions.setDeltaBS(BigDecimal.ZERO);
        positions.setDeltaPA(BigDecimal.ZERO);
        positions.setCTime(markPrice.getTs());
        positions.setUTime(markPrice.getTs());
        positions.setAdl(Integer.valueOf(0));
        positions.setIdxPx(BigDecimal.ZERO);
        positions.setImr(BigDecimal.ZERO);
        positions.setInterest(BigDecimal.ZERO);
        positions.setLast(BigDecimal.ZERO);
        positions.setLever(BigDecimal.ZERO);
        positions.setLiab(BigDecimal.ZERO);
//            positions.setLiabCcy();
        positions.setLiqPenalty(BigDecimal.ZERO);
        positions.setLiqPx(BigDecimal.ZERO);
        positions.setMarkPx(BigDecimal.ZERO);
        positions.setPos(BigDecimal.ZERO);
        positions.setPnl(BigDecimal.ZERO);

        return positions;
    }

}
