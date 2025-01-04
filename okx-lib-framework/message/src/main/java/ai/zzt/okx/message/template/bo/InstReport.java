package ai.zzt.okx.message.template.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2025/1/4
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstReport {

    /**
     * id
     */
    private String id;

    /**
     * 币种ID
     */
    private String instId;

    /**
     * 币种名称
     */
    private String name;

    /**
     * 日涨幅
     */
    private BigDecimal dailyRise;

    /**
     * 最近小时涨幅
     */
    private BigDecimal lastHourlyRise;

}
