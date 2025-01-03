package ai.zzt.okx.v5.entity.rest.account.resp;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.math.BigDecimal;

/**
 * 设置杠杆倍数相应参数
 *
 * @author zhouzhitong
 * @since 2024/10/23
 **/
@Data
public class LeverageResp implements IOkxRestRsp {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 币种，用于币种维度的杠杆。
     * 仅适用于现货模式/跨币种保证金模式/组合保证金模式 的全仓币币杠杆。
     */
    @Nullable
    private String ccy;

    /**
     * 杠杆倍数
     */
    private BigDecimal lever;

    /**
     * 保证金模式
     */
    private MgnMode mgnMode;

    /**
     * 持仓方向
     */
    private PositionsSide posSide;

}
