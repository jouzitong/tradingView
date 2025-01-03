package ai.zzt.okx.calculate.utils;

/**
 * @author zhouzhitong
 * @since 2024/6/2
 **/
public class MacdUtils {


//    /**
//     * MACD算法：
//     * DIF：EMA(short) - EMA(long) 一般short取12，long取26
//     * DEA: EMA(DIF, mid), mid一般取9
//     * MACD:(DIF-DEA)*2
//     *
//     * @param entries
//     * @param s  short
//     * @param l long
//     * @param m  mid
//     * @return
//     */
//    public static List[] getMACD(List<CandleEntry> entries, int s, int l, int m) {
//        ArrayList<Entry> listDIF = new ArrayList<>();
//        ArrayList<Entry> listDEA = new ArrayList<>();
//        ArrayList<BarEntry> listMACD = new ArrayList<>();
//
//        float lastEmaS = entries.get(0).getClose();
//        float lastEmaL = lastEmaS;
//        float lastDIF = 0;
//        listDIF.add(new Entry(0, 0));
//        listDEA.add(new Entry(0, 0));
//        listMACD.add(new BarEntry(0, 0));
//
//        float[] factorShort = getEMAFactor(s);
//        float[] factorLong = getEMAFactor(l);
//        float[] factorMid = getEMAFactor(m);
//        for (int i = 1; i < entries.size(); i++) {
//            float x = entries.get(i).getX();
//            // 短线EMA
//            float valueS = factorShort[0] * entries.get(i).getClose() + factorShort[1] * lastEmaS;
//            lastEmaS = valueS;
//            // 长线EMA
//            float valueL = factorLong[0] * entries.get(i).getClose() + factorLong[1] * lastEmaL;
//            lastEmaL = valueL;
//            // DIF：EMA(short) - EMA(long)
//            float valueDIF = valueS - valueL;
//            listDIF.add(new Entry(x, valueDIF));
//            // EMA(DIF, mid)
//            float valueDEA = factorMid[0] * valueDIF + factorMid[1] * lastDIF;
//            listDEA.add(new Entry(x, valueDEA));
//            lastDIF = valueDEA;
//            // MACD:(DIF-DEA)*2
//            listMACD.add(new BarEntry(x, (valueDIF - valueDEA) * 2));
//        }
//        return new ArrayList[]{listDIF, listDEA, listMACD};
//    }
}
