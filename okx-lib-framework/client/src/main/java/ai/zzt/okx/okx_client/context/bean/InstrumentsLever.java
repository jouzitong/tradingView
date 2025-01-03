package ai.zzt.okx.okx_client.context.bean;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品杠杆
 *
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Data
@NoArgsConstructor
public class InstrumentsLever {

    @JsonIgnore
    private String instId;

    @JsonIgnore
    private InstrumentType type;

    /**
     * 全仓杠杆
     */
    private Map<PositionsSide, BigDecimal> crossLeverMap = new ConcurrentHashMap<>();

    /**
     * 逐仓杠杆
     */
    private Map<PositionsSide, BigDecimal> isolatedLeverMap = new ConcurrentHashMap<>();

    public InstrumentsLever(String instId, InstrumentType type) {
        this.instId = instId;
        this.type = type;
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
