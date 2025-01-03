package ai.zzt.okx.emulator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/9/23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResult implements Serializable {

    /**
     * 最终收益
     */
    private BigDecimal lastProfit = BigDecimal.ZERO;

    /**
     * 单次 最大收益率
     */
    private BigDecimal maxProfit = BigDecimal.ZERO;

    /**
     * 单次 最大回撤率
     */
    private BigDecimal maxRetrace = BigDecimal.ZERO;

}
