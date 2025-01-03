package ai.zzt.okx.settings.calculate.strategy.demo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MaxPriceDifferenceCalculator {
    public static void main(String[] args) {
        // 示例数据
        List<PriceData> data = Arrays.asList(
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 0, 10), 100.5),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 0, 11), 101),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 0, 12), 109),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 0, 30), 101.0),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 1, 5), 98.0),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 1, 45), 102.0),
                new PriceData(LocalDateTime.of(2024, 12, 13, 10, 2, 10), 99.5)
        );

        // 设置时间间隔（例如1分钟）
        long intervalMinutes = 1;

        // 计算每个时间区间的最大差值
        Map<LocalDateTime, Double> maxDifferences = calculateMaxDifferences(data, intervalMinutes);

        // 输出结果
        maxDifferences.forEach((startTime, maxDiff) ->
                System.out.println("时间区间开始: " + startTime + ", 最大差值: " + maxDiff)
        );
    }

    public static Map<LocalDateTime, Double> calculateMaxDifferences(List<PriceData> data, long intervalMinutes) {
        // 按时间间隔分组
        Map<LocalDateTime, List<PriceData>> groupedData = data.stream()
                .collect(Collectors.groupingBy(d -> d.time.truncatedTo(ChronoUnit.MINUTES)
                        .plusMinutes((d.time.getSecond() / (intervalMinutes * 60)) * intervalMinutes)));

        // 计算每组的最大差值
        Map<LocalDateTime, Double> maxDifferences = new HashMap<>();
        for (Map.Entry<LocalDateTime, List<PriceData>> entry : groupedData.entrySet()) {
            List<PriceData> prices = entry.getValue();
            double maxPrice = prices.stream().mapToDouble(p -> p.price).max().orElse(0);
            double minPrice = prices.stream().mapToDouble(p -> p.price).min().orElse(0);
            maxDifferences.put(entry.getKey(), maxPrice - minPrice);
        }

        return maxDifferences;
    }
}

// 数据结构类
class PriceData {
    LocalDateTime time;
    double price;

    public PriceData(LocalDateTime time, double price) {
        this.time = time;
        this.price = price;
    }
}