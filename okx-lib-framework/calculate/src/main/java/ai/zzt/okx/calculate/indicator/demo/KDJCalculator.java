package ai.zzt.okx.calculate.indicator.demo;

import java.util.ArrayList;
import java.util.List;

class KDJCalculator {
    // 计算 RSV 值
    public static double calculateRSV(List<Double> highPrices, List<Double> lowPrices, List<Double> closePrices, int n) {
        double highestHigh = Double.MIN_VALUE;
        double lowestLow = Double.MAX_VALUE;
        for (int i = closePrices.size() - n; i < closePrices.size(); i++) {
            highestHigh = Math.max(highestHigh, highPrices.get(i));
            lowestLow = Math.min(lowestLow, lowPrices.get(i));
        }
        double rsv = ((closePrices.get(closePrices.size() - 1) - lowestLow) / (highestHigh - lowestLow)) * 100;
        return rsv;
    }

    // 计算 KDJ 值
    public static void calculateKDJ(List<Double> highPrices, List<Double> lowPrices, List<Double> closePrices, int n, int m1, int m2) {
        List<Double> kValues = new ArrayList<>();
        List<Double> dValues = new ArrayList<>();
        List<Double> jValues = new ArrayList<>();
        double k = 50;
        double d = 50;
        for (int i = n - 1; i < closePrices.size(); i++) {
            double rsv = calculateRSV(highPrices.subList(0, i + 1), lowPrices.subList(0, i + 1), closePrices.subList(0, i + 1), n);
            k = (2.0 / m2) * k + (1.0 / m1) * rsv;
            d = (2.0 / m1) * d + (1.0 / m2) * k;
            double j = 3 * k - 2 * d;
            kValues.add(k);
            dValues.add(d);
            jValues.add(j);
        }
        System.out.println("K values: " + kValues);
        System.out.println("D values: " + dValues);
        System.out.println("J values: " + jValues);
    }

    public static void main(String[] args) {
        List<Double> highPrices = new ArrayList<>();
        List<Double> lowPrices = new ArrayList<>();
        List<Double> closePrices = new ArrayList<>();
        // 这里假设添加了一些历史的价格数据，为了演示方便，添加了一些示例数据
        highPrices.add(100.0);
        highPrices.add(105.0);
        highPrices.add(110.0);
        // 依此类推添加更多数据
        lowPrices.add(90.0);
        lowPrices.add(92.0);
        lowPrices.add(95.0);
        // 依此类推添加更多数据
        closePrices.add(98.0);
        closePrices.add(102.0);
        closePrices.add(108.0);
        // 依此类推添加更多数据
        calculateKDJ(highPrices, lowPrices, closePrices, 9, 3, 3);
    }
}