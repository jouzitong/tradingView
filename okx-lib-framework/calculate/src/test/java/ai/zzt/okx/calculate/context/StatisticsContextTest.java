package ai.zzt.okx.calculate.context;

import ai.zzt.okx.common.Application;
import ai.zzt.okx.common.enums.TrendType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/8/7
 **/
@SpringBootTest(classes = Application.class)
public class StatisticsContextTest {

    @Test
    public void test() {
        long ts = System.currentTimeMillis();
        StatisticsContext statisticsContext = new StatisticsContext();
        statisticsContext.handle(new MarkPrice(null, "BTC", BigDecimal.valueOf(1), ts), TrendType.FLUCTUATE);
        statisticsContext.handle(new MarkPrice(null, "BTC", BigDecimal.valueOf(1), ts = ts + Bar.H_1.getScale()), TrendType.FLUCTUATE);
        statisticsContext.handle(new MarkPrice(null, "BTC", BigDecimal.valueOf(2), ts = ts + Bar.H_1.getScale()), TrendType.UP);
        statisticsContext.handle(new MarkPrice(null, "BTC", BigDecimal.valueOf(1), ts = ts + Bar.H_1.getScale()), TrendType.DOWN);
        statisticsContext.handle(new MarkPrice(null, "BTC", BigDecimal.valueOf(1), ts = ts + Bar.D_1.getScale()), TrendType.DOWN);
        System.out.println(statisticsContext);
    }

}
