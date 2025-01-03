package ai.zzt.okx.v5.entity.rest.account.req;

import lombok.Data;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.math.BigDecimal;

/**
 * <a href="https://www.okx.com/docs-v5/zh/#trading-account-rest-api-set-leverage">设置杠杆倍数请求参数</a>
 *
 * @author zhouzhitong
 * @since 2024/10/23
 **/
@Data
public class LeverageReq implements IOkxRestReq {

    private String instId;

    private String ccy;

    private BigDecimal lever;

    private MgnMode mgnMode;

    private PositionsSide posSide;

}
