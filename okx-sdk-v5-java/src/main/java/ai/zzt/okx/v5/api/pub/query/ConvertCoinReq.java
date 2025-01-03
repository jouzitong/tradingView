package ai.zzt.okx.v5.api.pub.query;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Jou Ziton
 * @since 2024/6/18
 **/
@Data
public class ConvertCoinReq {

    /**
     * 转换类型
     * 1：币转张
     * 2：张转币
     * 默认为1
     */
    private Integer type;

    /**
     * 产品ID，仅适用于交割/永续/期权
     */
    private String instId;

    /**
     * 数量，币转张时，为币的数量，张转币时，为张的数量。
     */
    private BigDecimal sz;

    /**
     * 委托价格
     * 币本位合约的张币转换时必填
     * U本位合约，usdt 与张的转换时，必填；coin 与张的转换时，可不填
     * 期权的张币转换时，可不填。
     */
    private BigDecimal px;

    /**
     * 币的单位
     * coin：币
     * usds：usdt/usdc
     * 仅适用于交割/永续的U本位合约
     */
    private String unit;

    /**
     * 将要下单的类型
     * open：开仓时将sz舍位
     * close：平仓时将sz四舍五入
     * 默认值为close
     * 适用于交割/永续
     */
    private String opType;

}
