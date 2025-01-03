package ai.zzt.okx.trade_order.engine.impl;

import ai.zzt.okx.settings.order.profit.GreedyOcoSettings;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024-08-27
 **/
//@SpringBootTest(classes = Application.class)
@Slf4j
public class BaseOrderEngineTest {

    @Test
    public void testProfit() {
        GreedyOcoSettings greedyOco = new GreedyOcoSettings();

        BigDecimal profitRate = BigDecimal.valueOf(0.1);
        BigDecimal nextRate = profitRate.subtract(greedyOco.getGreedyOcoRate());
        BigDecimal count = nextRate.divide(greedyOco.getGreedyOcoRateTrigger(), 0, RoundingMode.HALF_DOWN);

        log.info("profitRate: {}, nextRate: {}, count: {}", profitRate, nextRate, count);
        BigDecimal tp = profitRate.subtract(greedyOco.getGreedyOcoRateTrigger());

        log.info("count: {}", count);
        // 0.1 - 0.005 = 0.095
        //
    }

}
