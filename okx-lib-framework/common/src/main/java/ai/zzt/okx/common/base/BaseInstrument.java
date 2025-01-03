package ai.zzt.okx.common.base;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhouzhitong
 * @since 2024/11/14
 **/
public abstract class BaseInstrument {

    /**
     * 产品ID
     */
    @Getter
    private String ccy;

    /**
     * TODO 基础货币. 目前默认都是 USDT
     */
    @Getter
    @Setter
    private String baseCcy = "USDT";

    /**
     * 产品类型
     */
    @Getter
    @Setter
    private InstrumentType instType;

    public void setInstId(String instId) {
        this.ccy = InstUtils.getBase(instId);
        this.instType = InstUtils.getType(instId);
        this.baseCcy = InstUtils.getBase(instId);
    }

    public String getInstId() {
        return ccy + InstUtils.SPIT +
                baseCcy + InstUtils.SPIT +
                instType.value();
    }

}
