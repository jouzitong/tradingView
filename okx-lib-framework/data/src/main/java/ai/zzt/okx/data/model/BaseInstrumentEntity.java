package ai.zzt.okx.data.model;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zhouzhitong
 * @since 2024/11/14
 **/
@Getter
@Setter
public abstract class BaseInstrumentEntity extends BaseEntity {

    /**
     * 产品ID
     */
    @Field("inst_id")
    private String instId;

    /**
     * TODO 基础货币. 目前默认都是 USDT
     */
    @Field("base_ccy")
    private String baseCcy = "USDT";

    /**
     * 产品类型
     */
    @Field("inst_type")
    private InstrumentType instType;

    public void setInstId(String instId) {
        this.instId = InstUtils.getBase(instId);
        this.instType = InstUtils.getType(instId);
        this.baseCcy = InstUtils.getBase(instId);
    }
}
