package ai.zzt.okx.settings.calculate.face;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.KDJSettings;
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
public class KDJSettingsFace extends BaseCalculateSettingsFace<KDJSettings> {

    public KDJSettingsFace(String id) {
        super(id);
    }

    public KDJSettingsFace(Map<Bar, KDJSettings> settingsMap) {
        this.barSettingsMap = settingsMap;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return IndicatorType.KDJ;
    }

}
