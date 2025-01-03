package ai.zzt.okx.dispatcher.request;

import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/6/6
 **/
@Data
@NoArgsConstructor
public class OrderRequest {

    /**
     * 合约ID
     */
    @JsonIgnore
    private String instId;

    /**
     * 操作方向
     */
    private PositionsSide positionsSide;

    /**
     * 订单类型. 如果是空, 为市价单
     */
    private OrdType ordType;

    /**
     * 操作价格(buy/sell). 订单类型 = OrdType.CONDITIONAL 时, 起作用
     */
    private BigDecimal operatePrice;

    public OrderRequest(String instId) {
        this.instId = instId;
    }

    public void clear() {
        this.operatePrice = null;
        this.positionsSide = null;
        this.ordType = null;
    }

    @Override
    public OrderRequest clone() {
        return JackJsonUtils.clone(this);
    }

}
