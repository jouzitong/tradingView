package ai.zzt.okx.data.ws.vo;

import lombok.Data;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Data
public class TickersVO {

    private String instId;

    private InstrumentType instType;

    private BigDecimal markPx;

    private long ts;

}
