package ai.zzt.okx.v5.entity.rest.pub;

import lombok.Data;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;

import java.math.BigDecimal;

/**
 * @author Jou Ziton
 * @since 2024/6/18
 **/
@Data
public class ConvertContractCoin implements IOkxRestRsp {

    /**
     * 转换类型
     * 1：币转张
     * 2：张转币
     */
    private String type;

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 委托价格
     */
    private BigDecimal px;

    /**
     * 数量
     */
    private BigDecimal sz;

    /**
     * 币的单位
     * coin：币
     * usds：usdt/usdc
     */
    private String unit;

}
