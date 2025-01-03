package ai.zzt.okx.settings.context.face;

import ai.zzt.okx.common.utils.vo.DateTimeInterval;
import ai.zzt.okx.settings.context.SettingsContext;
import org.junit.jupiter.api.Test;

/**
 * @author zhouzhitong
 * @since 2024-08-19
 **/
public class SettingsContextFaceTest {

    @Test
    public void test() {
        SettingsContextStageStrategy settingsContextFace = new SettingsContextStageStrategy();

        settingsContextFace.addSettings(new DateTimeInterval(1L, 2), new SettingsContext("BTC1", false));
        settingsContextFace.addSettings(new DateTimeInterval(6L, 4), new SettingsContext("BTC6", false));
        settingsContextFace.addSettings(new DateTimeInterval(9L, 4), new SettingsContext("BTC9", false));
        settingsContextFace.addSettings(new DateTimeInterval(0L, 0), new SettingsContext("BTC0", false));
        settingsContextFace.addSettings(new DateTimeInterval(111L, 9), new SettingsContext("BTC111", false));
        System.out.println(settingsContextFace.get(0L));
        System.out.println(settingsContextFace.get(2L));
        System.out.println(settingsContextFace.get(6L));
        System.out.println(settingsContextFace.get(3L));
        System.out.println(settingsContextFace.get(11L));
        System.out.println(settingsContextFace.get(1111L));
    }

}
