package ai.zzt.okx.okx_client.utils;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024/10/23
 **/
public class SwapCoinUtils {

    /**
     * 币 ---> 张
     *
     * @param instId 产品ID
     * @param cash   资金
     * @param mp     价格
     * @return 张的数量
     */
    public static BigDecimal coin(String instId, BigDecimal cash, BigDecimal mp, BigDecimal lever) {
        BigDecimal sz = cash.divide(mp, 10, RoundingMode.FLOOR).multiply(lever);
        Instruments instruments = InstrumentContext.get(instId);
        if (instruments == null || instruments.getInstType() != InstrumentType.SWAP) {
            throw new TodoRuntimeException();
        }
        int scale = instruments.getLotSz().scale();
        // 每张 = ctVal 币
        BigDecimal ctVal = instruments.getCtVal();
        return sz.divide(ctVal, scale, RoundingMode.HALF_UP);
    }

    /**
     * 币 ---> 张
     *
     * @param instId 产品ID
     * @param sz     币的数量
     * @return 张的数量
     */
    public static BigDecimal coin(String instId, BigDecimal sz) {
        Instruments instruments = InstrumentContext.get(instId);
        if (instruments == null || instruments.getInstType() != InstrumentType.SWAP) {
            throw new TodoRuntimeException();
        }
        int scale = instruments.getLotSz().scale();
        // 每张 = ctVal 币
        BigDecimal ctVal = instruments.getCtVal();
        return ctVal.divide(sz, scale, RoundingMode.HALF_UP);
    }

    /**
     * 张 ---> 币
     *
     * @param instId 产品ID
     * @param ctSz   张的数量
     * @return 币的数量
     */
    public static BigDecimal ccy(String instId, @Nullable BigDecimal ctSz) {
        if (ctSz == null) {
            return BigDecimal.ZERO;
        }
        Instruments instruments = InstrumentContext.get(instId);
        if (instruments.getInstType() == InstrumentType.SWAP) {
            int scale = instruments.getLotSz().scale();
            // 每张 = ctVal 币
            BigDecimal ctVal = instruments.getCtVal();
            return ctSz.multiply(ctVal);
        } else if (instruments.getInstType() == InstrumentType.MARGIN) {
            return ctSz;
        }
        throw new TodoRuntimeException();
    }

}
