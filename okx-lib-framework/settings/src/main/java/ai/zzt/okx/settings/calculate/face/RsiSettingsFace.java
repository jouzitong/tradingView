package ai.zzt.okx.settings.calculate.face;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.RsiSettings;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/7/21
 **/
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class RsiSettingsFace extends BaseCalculateSettingsFace<RsiSettings> {

    public RsiSettingsFace(String id) {
        super(id);
    }

    public RsiSettingsFace(Map<Bar, RsiSettings> settingsMap) {
        this.barSettingsMap = settingsMap;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return IndicatorType.RSI;
    }

}
