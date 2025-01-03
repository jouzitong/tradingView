package ai.zzt.okx.v5.entity.rest.trading.order.booking.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseOrderReq implements IOkxRestReq {

    /**
     * 产品ID，如 BTC-USDT
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 持仓方向
     * 买卖模式下：可不填写此参数，默认值net，如果填写，仅可以填写net
     * 开平仓模式下： 必须填写此参数，且仅可以填写 long：平多 ，short：平空
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    private PositionsSide posSide;

    /**
     * 保证金模式
     * cross：全仓 ； isolated：逐仓
     */
    @JSONField(name = "mgnMode")
    @JsonProperty("mgnMode")
    private MgnMode mgnMode;

    /**
     * 	保证金币种，单币种保证金模式的全仓币币杠杆平仓必填
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    private String ccy;

    /**
     * 	当市价全平时，平仓单是否需要自动撤销,默认为false.
     * false：不自动撤单 true：自动撤单
     */
    @JSONField(name = "autoCxl")
    @JsonProperty("autoCxl")
    private Boolean autoCxl;

    /**
     * 客户自定义ID
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     */
    @JSONField(name = "clOrdId")
    @JsonProperty("clOrdId")
    private String clOrdId;

    /**
     * 	订单标签
     * 字母（区分大小写）与数字的组合，可以是纯字母、纯数字，且长度在1-16位之间。
     */
    @JSONField(name = "tag")
    @JsonProperty("tag")
    private String tag;

}
