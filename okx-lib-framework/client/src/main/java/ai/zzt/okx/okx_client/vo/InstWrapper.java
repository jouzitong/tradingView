package ai.zzt.okx.okx_client.vo;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import lombok.Data;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品包装类
 *
 * @author zhouzhitong
 * @since 2024/10/23
 **/
@Data
@Deprecated
public class InstWrapper {

    public Instruments instruments;

    /**
     * 全仓杠杆
     */
    private Map<PositionsSide, BigDecimal> crossLeverMap;

    /**
     * 逐仓杠杆
     */
    private Map<PositionsSide, BigDecimal> isolatedLeverMap;

    public InstWrapper(Instruments instruments) {
        this.instruments = instruments;
        if (instruments.getInstType() == InstrumentType.SWAP || instruments.getInstType() == InstrumentType.MARGIN) {
            this.crossLeverMap = new ConcurrentHashMap<>();
            this.isolatedLeverMap = new ConcurrentHashMap<>();
        }
    }

    public void update(LeverageResp resp) {
        BigDecimal lever = resp.getLever();
        PositionsSide posSide = resp.getPosSide();
        MgnMode mgnMode = resp.getMgnMode();
        if (mgnMode == MgnMode.CROSS) {
            crossLeverMap.put(posSide, lever);
        } else if (mgnMode == MgnMode.ISOLATED) {
            isolatedLeverMap.put(posSide, lever);
        }
    }

    public void update(List<LeverageResp> resps) {
        for (LeverageResp resp : resps) {
            update(resp);
        }
    }

    public BigDecimal getLever(MgnMode mode, PositionsSide posSide) {
        if (mode == MgnMode.CROSS) {
            return crossLeverMap.get(posSide);
        } else if (mode == MgnMode.ISOLATED) {
            return isolatedLeverMap.get(posSide);
        }
        throw new TodoRuntimeException();
    }

}
