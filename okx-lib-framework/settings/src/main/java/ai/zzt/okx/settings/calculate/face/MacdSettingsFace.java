package ai.zzt.okx.settings.calculate.face;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.MacdSettings;
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
public class MacdSettingsFace extends BaseCalculateSettingsFace<MacdSettings> {

    public MacdSettingsFace(String id) {
        super(id);
    }

    public MacdSettingsFace(Map<Bar, MacdSettings> macdSettingsMap) {
        this.barSettingsMap = macdSettingsMap;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return IndicatorType.MACD;
    }

}
