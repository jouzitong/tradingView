package ai.zzt.okx.settings.model;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.utils.vo.DateTimeInterval;
import ai.zzt.okx.data.model.BaseEntity;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/8/1
 **/
@Getter
@Setter
@ToString(callSuper = true)
@Document(collection = "settings_context")
public class SettingsContextEntity extends BaseEntity {

    /**
     * 产品ID
     */
    @Field("inst_id")
    private String instId;

    /**
     * 产品类型
     */
    @Field("inst_type")
    @Deprecated
    private InstrumentType instType;

    /**
     * 时间粒度:
     */
    @Field("bars")
    private List<Bar> bars;

    /**
     * 计算配置
     */
    @Field("calculate_settings_face_map")
    private Map<IndicatorType, BaseCalculateSettingsFace<?>> calculateSettingsFaceMap;

    /**
     * 下单配置
     */
    @Field("place_order_settings")
    private PlaceOrderSettings placeOrderSettings;

    /**
     * 时间区间. 如果为空, 默认全时间段生效.
     */
    @Field("interval")
    private DateTimeInterval interval;

}
