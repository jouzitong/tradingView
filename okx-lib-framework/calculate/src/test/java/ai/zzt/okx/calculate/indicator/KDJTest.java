//package ai.zzt.okx.calculate.indicator;
//
//import ai.zzt.okx.calculate.indicator.val.KDJVal;
//import ai.zzt.okx.common.utils.SlideList;
//import ai.zzt.okx.v5.enumeration.Bar;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * @author zhouzhitong
// * @since 2024/12/2
// **/
//public class KDJTest {
//
//    @Test
//    public void test1() {
//        KDJ kdj = new KDJ(3, Bar.M_1, BigDecimal.valueOf(1.0 / 3));
//        List<BigDecimal> highs = List.of(new BigDecimal("10.0"), new BigDecimal("11.0"), new BigDecimal("12.0"),
//                new BigDecimal("13.0"), new BigDecimal("14.0"), new BigDecimal("15.0"), new BigDecimal("16.0"));
//
//        List<BigDecimal> lows = List.of(new BigDecimal("8.0"), new BigDecimal("7.0"), new BigDecimal("6.0"),
//                new BigDecimal("6.0"), new BigDecimal("7.0"), new BigDecimal("8.0"), new BigDecimal("9.0"));
//
//        List<BigDecimal> closes = List.of(new BigDecimal("9.0"), new BigDecimal("8.0"), new BigDecimal("10.0"),
//                new BigDecimal("12.0"), new BigDecimal("13.0"), new BigDecimal("14.0"), new BigDecimal("15.0"));
//        long scale = Bar.M_1.getScale();
//
//        for (int i = 0; i < highs.size(); i++) {
//            kdj.add(highs.get(i), scale + (i + 1));
//            kdj.add(lows.get(i), scale + (i + 2));
//            kdj.add(closes.get(i), scale + (i + 3));
//            scale += Bar.M_1.getScale();
//        }
//        SlideList<KDJVal> vals = kdj.getVals();
//        for (KDJVal val : vals) {
//            System.out.println(val);
//        }
//    }
//
//}
