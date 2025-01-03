package ai.zzt.okx.common.enums;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 变更程度
 *
 * @author zhouzhitong
 * @since 2024/6/9
 **/
@Getter
public enum Degree {

    // 大幅变动, 中等变动, 小幅变动
    BIG("大幅变动", BigDecimal.valueOf(0.1)),      // 1.0 <= 10 % 的变动, 认为是大幅变动
    MEDIUM("中等变动", BigDecimal.valueOf(0.01)),   // 0.5 - 1.0% 的变动, 认为是中等变动
    SMALL("小幅变动", BigDecimal.valueOf(0.005)),   // 0.0 - 0.5% 的变动, 认为是小幅变动
    ;

    private final String name;

    private final BigDecimal value;

    Degree(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }


}
