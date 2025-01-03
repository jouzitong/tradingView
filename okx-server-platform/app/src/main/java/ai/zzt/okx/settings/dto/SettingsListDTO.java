package ai.zzt.okx.settings.dto;

import lombok.Data;

/**
 * @author zhouzhitong
 * @since 2024/11/27
 **/
@Data
public class SettingsListDTO {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 能否自动下单
     */
    private boolean enablePlace;

}
