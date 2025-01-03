package ai.zzt.okx.settings.permutations;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.exception.DeprecatedRuntimeException;
import ai.zzt.okx.settings.calculate.BaseCalculateSettings;
import ai.zzt.okx.settings.calculate.BollSettings;
import ai.zzt.okx.settings.calculate.MacdSettings;
import ai.zzt.okx.settings.calculate.RsiSettings;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import ai.zzt.okx.settings.calculate.face.BollSettingsFace;
import ai.zzt.okx.settings.calculate.face.MacdSettingsFace;
import ai.zzt.okx.settings.calculate.face.RsiSettingsFace;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.settings.permutations.calculate.BollSettingsPermutations;
import ai.zzt.okx.settings.permutations.calculate.MacdSettingsPermutations;
import ai.zzt.okx.settings.permutations.calculate.RsiSettingsPermutations;
import ai.zzt.okx.settings.permutations.order.PlaceOrderSettingsPermutations;
import ai.zzt.okx.settings.permutations.param.ScrollParam;
import ai.zzt.okx.settings.utils.PermutationsUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ai.zzt.okx.v5.enumeration.Bar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 配置的排列组合
 *
 * @author zhouzhitong
 * @see SettingsContext  SettingsContext 详细的配置上下文
 * @since 2024/7/1
 **/
@Setter
@Getter
@ToString
public class SettingsPermutations implements Permutations<SettingsContext> {

    /**
     * 是否用排列组合. 默认 true 启用
     */
    private boolean enable = true;

//    /**
//     * TODO 排列组合数量限制
//     */
//    private int limit = 1000000;

    /**
     * 时间粒度
     */
    private List<Bar> bars = Arrays.asList(Bar.M_30, Bar.H_1, Bar.D_1);

    /**
     * 目标权重
     */
    private List<Integer> targetWeights = Arrays.asList(60, 80);

    /**
     * 下单排列组合配置
     */
    private PlaceOrderSettingsPermutations placeOrderSettings;

    /**
     * 计算配置排列组合配置
     */
    private MacdSettingsPermutations macdSettings;

    private RsiSettingsPermutations rsiSettings;

    private BollSettingsPermutations bollSettings;

    @Override
    public long size() {
        return toSettingsCount(null, new ScrollParam());
    }

    @Deprecated // 废弃, 一次性生成太大了. 请使用 toSettings(String instId, ScrollParam param)
    @Override
    public List<SettingsContext> toSettings(String instId) {
        throw new DeprecatedRuntimeException();
    }

    // TODO BUG: 会统计被筛选出去的配置
    public long toSettingsCount(String instId, ScrollParam param) {
        if (!enable) {
            return 1;
        }
        List<PlaceOrderSettings> placeOrderSettings = this.placeOrderSettings.toSettings(instId);

        List<MacdSettings> macdSettings = this.macdSettings.toSettings(instId);
        List<RsiSettings> rsiSettings = this.rsiSettings.toSettings(instId);
        List<BollSettings> bollSettings = this.bollSettings.toSettings(instId);

        List<List<Bar>> barCombinations = PermutationsUtils.getCombinationsTest(bars);

        long count = 0;
        int i = param.getIndexList().get(0);
        int psi = param.getIndexList().get(1);
        int msi = param.getIndexList().get(2);
        int rsii = param.getIndexList().get(3);
        int boi = param.getIndexList().get(4);
        int twi = param.getIndexList().get(5);
        try {
            for (; i < barCombinations.size(); i++) {
                List<Bar> bars = barCombinations.get(i);
                List<Map<Bar, MacdSettings>> macdMaps = PermutationsUtils.combineSets(bars, macdSettings);
                List<Map<Bar, RsiSettings>> rsiMaps = PermutationsUtils.combineSets(bars, rsiSettings);
                List<Map<Bar, BollSettings>> bollMaps = PermutationsUtils.combineSets(bars, bollSettings);
                count += 7L * bollMaps.size()
                        * rsiMaps.size()
                        * macdMaps.size()
                        * placeOrderSettings.size()
                        * targetWeights.size();
            }
            return count;
        } finally {
            param.updateIndex(i, psi, msi, rsii, boi, twi);
        }
    }

    @Override
    public List<SettingsContext> toSettings(String instId, ScrollParam param) {
        List<SettingsContext> res = new LinkedList<>();
        if (!enable) {
            res.add(new SettingsContext(instId, true));
            return res;
        }

        List<PlaceOrderSettings> placeOrderSettings = this.placeOrderSettings.toSettings(instId);

        List<MacdSettings> macdSettings = this.macdSettings.toSettings(instId);
        List<RsiSettings> rsiSettings = this.rsiSettings.toSettings(instId);
        List<BollSettings> bollSettings = this.bollSettings.toSettings(instId);

        List<List<Bar>> barCombinations = PermutationsUtils.getCombinationsTest(bars);
        int i = param.getIndexList().get(0);
        int psi = param.getIndexList().get(1);
        int msi = param.getIndexList().get(2);
        int rsii = param.getIndexList().get(3);
        int boi = param.getIndexList().get(4);
        int twi = param.getIndexList().get(5);

        try {
            for (; i < barCombinations.size(); i++) {
                List<Bar> bars = barCombinations.get(i);
                List<Map<Bar, MacdSettings>> macdMaps = PermutationsUtils.combineSets(bars, macdSettings);
                List<Map<Bar, RsiSettings>> rsiMaps = PermutationsUtils.combineSets(bars, rsiSettings);
                List<Map<Bar, BollSettings>> bollMaps = PermutationsUtils.combineSets(bars, bollSettings);
                for (; twi < targetWeights.size(); twi++) {
                    for (; psi < placeOrderSettings.size(); psi++) {
                        for (; msi < macdMaps.size(); msi++) {
                            for (; rsii < rsiMaps.size(); rsii++) {
                                for (; boi < bollMaps.size(); boi++) {
                                    PlaceOrderSettings placeOrderSetting = placeOrderSettings.get(psi);
                                    Map<Bar, MacdSettings> macdMap = macdMaps.get(msi);
                                    Map<Bar, RsiSettings> rsiMap = rsiMaps.get(rsii);
                                    Map<Bar, BollSettings> bollMap = bollMaps.get(boi);

                                    List<Map<Bar, ?>> indexList = new ArrayList<>();
                                    indexList.add(macdMap);
                                    indexList.add(rsiMap);
                                    indexList.add(bollMap);
                                    List<List<Map<Bar, ?>>> indexCombinations = PermutationsUtils.getCombinations(indexList);
                                    for (List<Map<Bar, ?>> indexCombination : indexCombinations) {
                                        SettingsContext context = new SettingsContext();
                                        context.setInstId(instId);
                                        context.setTargetWeight(targetWeights.get(twi));
                                        context.setPlaceOrderSettings(placeOrderSetting);
                                        Map<IndicatorType, BaseCalculateSettingsFace<?>> calculateSettingsFaceMap = new HashMap<>();
                                        context.setCalculateSettingsFaceMap(calculateSettingsFaceMap);
                                        int w = 0;
                                        for (Map<Bar, ?> barMap : indexCombination) {
                                            for (Bar bar : barMap.keySet()) {
                                                BaseCalculateSettings base = (BaseCalculateSettings) barMap.get(bar);
                                                if (base.getWeights() == 0) {
                                                    continue;
                                                }
                                                switch (base) {
                                                    case MacdSettings _ -> {
                                                        calculateSettingsFaceMap.put(IndicatorType.MACD, new MacdSettingsFace((Map<Bar, MacdSettings>) barMap));
                                                        w += calculateSettingsFaceMap.get(IndicatorType.MACD).getSettings(bar).getWeights();
                                                    }
                                                    case RsiSettings _ -> {
                                                        calculateSettingsFaceMap.put(IndicatorType.RSI, new RsiSettingsFace((Map<Bar, RsiSettings>) barMap));
                                                        w += calculateSettingsFaceMap.get(IndicatorType.RSI).getSettings(bar).getWeights();
                                                    }
                                                    case BollSettings _ -> {
                                                        calculateSettingsFaceMap.put(IndicatorType.BOLL, new BollSettingsFace((Map<Bar, BollSettings>) barMap));
                                                        w += calculateSettingsFaceMap.get(IndicatorType.BOLL).getSettings(bar).getWeights();
                                                    }
                                                    default -> {
                                                    }
                                                }
                                            }
                                        }
                                        if (w < targetWeights.get(i)) {
                                            continue;
                                        }
                                        res.add(context);
                                    }
                                    if (res.size() >= param.getSize()) {
                                        boi++;
                                        return res;
                                    }
                                }
                                boi = 0;
                            }
                            rsii = 0;
                        }
                        msi = 0;
                    }
                    psi = 0;
                }
                twi = 0;
            }
            return res;
        } finally {
            param.updateIndex(i, psi, msi, rsii, boi, twi);
        }
    }
}
