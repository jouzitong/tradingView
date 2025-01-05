package ai.zzt.okx.settings.context;

import ai.zzt.okx.common.base.Context;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.settings.calculate.BollSettings;
import ai.zzt.okx.settings.calculate.KDJSettings;
import ai.zzt.okx.settings.calculate.KSettings;
import ai.zzt.okx.settings.calculate.MacdSettings;
import ai.zzt.okx.settings.calculate.RsiSettings;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import ai.zzt.okx.settings.calculate.face.BollSettingsFace;
import ai.zzt.okx.settings.calculate.face.KDJSettingsFace;
import ai.zzt.okx.settings.calculate.face.KSettingsFace;
import ai.zzt.okx.settings.calculate.face.MacdSettingsFace;
import ai.zzt.okx.settings.calculate.face.RsiSettingsFace;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SettingsContext implements Context {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 产品类型
     */
    private InstrumentType instType;

    /**
     * 需要分析的时间周期
     */
    private List<Bar> bars = Arrays.asList(Bar.M_15);

    /**
     * 目标权重. 默认 80
     */
    private int targetWeight = 80;

    /**
     * 局部变量
     */
    @JsonIgnore
    private Bar localBar;

    /**
     * 计算配置
     */
    private Map<IndicatorType, BaseCalculateSettingsFace<?>> calculateSettingsFaceMap;

    /**
     * 下单配置
     */
    private PlaceOrderSettings placeOrderSettings;

    /**
     * 构造函数
     *
     * @param instId 产品ID
     * @param init   是否初始化. 不再支持该参数
     */
    public SettingsContext(String instId, @Deprecated boolean init) {
        this.instId = instId;
        if (init) {
            this.instType = InstrumentType.SWAP;
            initDefaultSettings();
        }
    }

    public <S extends BaseCalculateSettingsFace<?>> S get(IndicatorType calculateType) {
        return (S) calculateSettingsFaceMap.get(calculateType);
    }

    public List<Bar> getBars() {
        Bar.sort(bars);
        return bars;
    }

    @Nullable
    public Bar getLocalBar() {
        if (localBar != null) {
            return localBar;
        }
        if (CollectionUtils.isEmpty(bars)) {
            return null;
        }
        return localBar = bars.stream().min(Bar::compareTo).get();
    }

    private void initDefaultSettings() {
        placeOrderSettings = new PlaceOrderSettings(instId);
        calculateSettingsFaceMap = new HashMap<>();
        RsiSettingsFace rsiSettingsFace = new RsiSettingsFace(instId);
        MacdSettingsFace macdSettingsFace = new MacdSettingsFace(instId);
        KSettingsFace kSettingsFace = new KSettingsFace(instId);
        BollSettingsFace bollSettingsFace = new BollSettingsFace(instId);
        KDJSettingsFace kdjSettingsFace = new KDJSettingsFace(instId);
        calculateSettingsFaceMap.put(IndicatorType.RSI, rsiSettingsFace);
        calculateSettingsFaceMap.put(IndicatorType.MACD, macdSettingsFace);
        calculateSettingsFaceMap.put(IndicatorType.K_LINE, kSettingsFace);
        calculateSettingsFaceMap.put(IndicatorType.BOLL, bollSettingsFace);
        calculateSettingsFaceMap.put(IndicatorType.KDJ, kdjSettingsFace);
        for (Bar bar : getBars()) {
            rsiSettingsFace.addSettings(bar, new RsiSettings(instId, instType));
            macdSettingsFace.addSettings(bar, new MacdSettings(instId, instType));
            kSettingsFace.addSettings(bar, new KSettings(instId, instType));
            bollSettingsFace.addSettings(bar, new BollSettings(instId, instType));
            kdjSettingsFace.addSettings(bar, new KDJSettings(instId, instType));
        }
    }

    @Override
    public String toString() {
        return JackJsonUtils.toStr(this);
    }

    public SettingsContext clone() {
        return JackJsonUtils.toBean(JackJsonUtils.toStr(this), SettingsContext.class);
    }

//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("instId: ").append(instId).append("\n");
//        sb.append("下单配置: \n\t").append(placeOrderSettings);
//        sb.append("\n");
//
//        sb.append("计算配置: \n");
//        calculateSettingsFaceMap.forEach((k, v) -> {
//            sb.append(k.name()).append(": \n\t").append(v).append("\n");
//        });
//        return sb.toString();
//    }

}
