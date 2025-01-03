package ai.zzt.okx.settings.calculate.face;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.BollSettings;
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
public class BollSettingsFace extends BaseCalculateSettingsFace<BollSettings> {

    public BollSettingsFace(String id) {
        super(id);
    }

    public BollSettingsFace(Map<Bar, BollSettings> bollSettingsMap) {
        this.barSettingsMap = bollSettingsMap;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return IndicatorType.BOLL;
    }

}
