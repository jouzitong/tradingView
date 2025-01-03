package ai.zzt.okx.storage.engine.demo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CandleStorage {
    private final int maxSize;
    private TreeMap<Long, Candle> candleMap;

    public CandleStorage(int maxSize) {
        this.maxSize = maxSize;
        this.candleMap = new TreeMap<>();
    }

    public void addCandle(Candle candle) {
        if (candleMap.size() >= maxSize) {
            // 删除最旧的 K 线数据
            candleMap.remove(candleMap.firstKey());
        }
        candleMap.put(candle.getTimestamp(), candle);
    }

    public void saveToFile(String filename) throws IOException {
        try (Writer writer = new FileWriter(filename)) {
        }
    }

    public void loadFromFile(String filename) throws IOException {
        try (Reader reader = new FileReader(filename)) {
//            List<Candle> candles = gson.fromJson(reader, new TypeToken<List<Candle>>(){}.getType());
//            for (Candle candle : candles) {
//                addCandle(candle); // 使用 addCandle 方法以保持大小限制
//            }
        }
    }

    public List<Candle> queryCandles(long startTime, long endTime) {
        return new ArrayList<>(candleMap.subMap(startTime, endTime).values());
    }

    public static void main(String[] args) {
        CandleStorage storage = new CandleStorage(100); // 设置最大容量为100

        // 添加一些示例K线数据
        long now = System.currentTimeMillis();
        for (int i = 0; i < 105; i++) { // 添加105条数据，超出限制以测试删除功能
            storage.addCandle(new Candle(now + i * 1000, 100 + i, 110 + i, 115 + i, 95 + i));
        }

        try {
            storage.saveToFile("candles.json");
            storage.loadFromFile("candles.json");

            // 查询指定时间范围内的K线数据
            List<Candle> queriedCandles = storage.queryCandles(now + 90000, now + 120000);

            // 打印查询结果
            queriedCandles.forEach(candle -> System.out.println(candle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
