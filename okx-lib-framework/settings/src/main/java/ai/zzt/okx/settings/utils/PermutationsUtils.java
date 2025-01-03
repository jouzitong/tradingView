package ai.zzt.okx.settings.utils;

import ai.zzt.okx.v5.enumeration.Bar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 排列组合工具类
 *
 * @author zhouzhitong
 * @since 2024/7/3
 **/
public class PermutationsUtils {

    /**
     * 获取所有的排列组合
     *
     * @param dataList 数据列表
     * @return
     */
    public static <T> List<List<T>> getCombinations(List<T> dataList) {
        Set<List<T>> combinations = new HashSet<>();
        combine(dataList, 0, new ArrayList<>(), combinations);
        return new ArrayList<>(combinations);
    }

    public static <T> List<List<T>> getCombinationsTest(List<T> dataList) {
        List<List<T>> combinations = new ArrayList<>();
        for (T t : dataList) {
            combinations.add(Collections.singletonList(t));
        }
        return combinations;
    }

    /**
     * 获取所有的排列组合. 基于集合A, 进行组合, 结果为Map<Bar, T>
     *
     * @param setA 集合A
     * @param setB 集合B
     * @param <T>  类型
     * @return 排列组合
     */
    public static <T> List<Map<Bar, T>> combineSets(List<Bar> setA, List<T> setB) {
        if (setB == null || setB.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<Bar, T>> result = new ArrayList<>();

        // 生成集合B的所有大小为setA.size()的组合
        List<List<T>> combinations = new ArrayList<>();
        generateCombinations(setB, setA.size(), 0, new ArrayList<>(), combinations);

        // 对每个组合生成排列并与集合A匹配
        for (List<T> combination : combinations) {
            boolean[] used = new boolean[combination.size()];
            permute(setA, combination, new ArrayList<>(), used, result);
        }

        return result;

    }

    // 方法用于生成集合B中大小为k的所有组合
    public static <T> void generateCombinations(List<T> setB, int k, int start, List<T> currentCombination, List<List<T>> combinations) {
        if (currentCombination.size() == k) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = start; i < setB.size(); i++) {
            currentCombination.add(setB.get(i));
            generateCombinations(setB, k, i + 1, currentCombination, combinations);
            currentCombination.remove(currentCombination.size() - 1);  // 回溯
        }
    }

    // 递归生成组合的所有排列
    public static <T> void permute(List<Bar> setA, List<T> setBSubset, List<T> currentPermutation, boolean[] used, List<Map<Bar, T>> result) {
        if (currentPermutation.size() == setA.size()) {
            Map<Bar, T> map = new HashMap<>();
            for (int i = 0; i < setA.size(); i++) {
                map.put(setA.get(i), currentPermutation.get(i));
            }
            result.add(map);
            return;
        }

        for (int i = 0; i < setBSubset.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                currentPermutation.add(setBSubset.get(i));
                permute(setA, setBSubset, currentPermutation, used, result);
                currentPermutation.remove(currentPermutation.size() - 1);  // 回溯
                used[i] = false;
            }
        }
    }

    // Helper method to generate all combinations
    private static <T> void combine(List<T> nums, int start, List<T> current, Set<List<T>> result) {
        if (!current.isEmpty()) {
            result.add(new ArrayList<>(current));
        }

        for (int i = start; i < nums.size(); i++) {
            current.add(nums.get(i));
            combine(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }


}
