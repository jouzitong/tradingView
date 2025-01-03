package ai.zzt.okx.settings.permutations.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.BaseCalculateSettings;
import ai.zzt.okx.settings.permutations.Permutations;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 计算基础配置【排列组合】
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MacdSettingsPermutations.class, name = "MACD"),
        @JsonSubTypes.Type(value = RsiSettingsPermutations.class, name = "RSI"),
        @JsonSubTypes.Type(value = BollSettingsPermutations.class, name = "BOLL"),
})
public abstract class BaseCalculateSettingsPermutations<T extends BaseCalculateSettings> implements Permutations<T> {

    /**
     * 权重
     */
    protected List<Integer> weights = Arrays.asList(70, 80, 90);

    @JsonGetter
    public abstract IndicatorType type();

}
