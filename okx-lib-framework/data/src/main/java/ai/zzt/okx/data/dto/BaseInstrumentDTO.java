package ai.zzt.okx.data.dto;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Setter
@Getter
@ToString(callSuper = true)
public class BaseInstrumentDTO extends BaseDTO {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * TODO 基础货币. 目前默认都是 USDT
     */
    private String baseCcy = "USDT";

    /**
     * 产品类型
     */
    private InstrumentType instType;

    public void setInstId(String instId) {
        this.instId = InstUtils.getBase(instId);
        this.instType = InstUtils.getType(instId);
        this.baseCcy = InstUtils.getBase(instId);
    }

}
