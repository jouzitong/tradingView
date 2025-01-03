package ai.zzt.okx.settings.calculate.face;

import ai.zzt.okx.common.base.settings.BaseSettings;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.BaseCalculateSettings;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.v5.enumeration.Bar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/7/21
 **/
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "indicatorType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MacdSettingsFace.class, name = "MACD"),
        @JsonSubTypes.Type(value = RsiSettingsFace.class, name = "RSI"),
        @JsonSubTypes.Type(value = BollSettingsFace.class, name = "BOLL"),
        @JsonSubTypes.Type(value = KSettingsFace.class, name = "K line"),
        @JsonSubTypes.Type(value = KDJSettingsFace.class, name = "KDJ"),
})
public abstract class BaseCalculateSettingsFace<S extends BaseCalculateSettings> extends BaseSettings {


    /**
     * 是否启用计算配置, 默认: true
     */
    private boolean enable = true;

    /**
     * 计算周期具体的配置
     * <p>
     * key: Bar 计算配置支持的计算周期. 这个值的范围必须要 >= {@link SettingsContext#getBars()} 的范围, 否则会导致计算出错.
     * <p>
     * value: 具体的配置
     */
    protected Map<Bar, S> barSettingsMap;

    public BaseCalculateSettingsFace(String id) {
        super(id);
        this.barSettingsMap = new HashMap<>();
    }

    public void addSettings(Bar bar, S settings) {
        barSettingsMap.put(bar, settings);
    }

    public S getSettings(Bar bar) {
        return barSettingsMap.get(bar);
    }

    /**
     * 计算类型
     *
     * @return 计算类型
     */
    public abstract IndicatorType getIndicatorType();

    @JsonIgnore
    public Collection<Bar> getBars() {
        return barSettingsMap.keySet();
    }

}
