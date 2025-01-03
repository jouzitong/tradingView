package ai.zzt.okx.emulator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/8/4
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSettings {

    /**
     * 开启爆仓检查. 默认 true
     */
    private boolean bloomEnable = true;

    /**
     * TODO 模拟器测试任务及其下单数据是否持久化. 默认 false
     * <p>
     * TODO 期望: 模拟器测试任务的下单数据配置, 应该分开管理. 例如: 任务持久化 = true, 下单数据持久化 = false.
     */
    private boolean storeEnable = false;

    /**
     * 手续费率. 默认 0.07%
     */
    private BigDecimal feeRate = BigDecimal.valueOf(0.00_07);

    /**
     * 初始资金. 默认 100
     */
    private BigDecimal initCash = BigDecimal.valueOf(100);

    /**
     * 目标回撤金额. 默认 5
     * <p>
     * 描述: 当回撤金额大于目标回撤金额时, 任务会进行适当的信息记录, 提醒用户该任务有问题, 需要解决
     * <p>
     * TODO 应该是回撤率
     */
    private BigDecimal retrace = BigDecimal.valueOf(-0.07);

    /**
     * 模拟延迟. 默认 0 没有延迟
     */
    private long delay = 0;

}
