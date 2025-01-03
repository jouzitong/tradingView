package ai.zzt.okx.okx_client.type;

import ai.zzt.okx.common.base.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 线类型
 *
 * @author zhouzhitong
 * @since 2024/12/10
 **/
@Getter
public enum LineType implements IEnum {

    /**
     * 描述: 简单移动平均线
     */
    SMA(1, "SMA"),
    /**
     * 描述: 指数移动平均线
     */
    EMA(2, "EMA"),
    /**
     * 描述: 加权移动平均线
     */
    WMA(3, "WMA"),
    ;

    private final int code;

    @JsonValue
    private final String name;

    LineType(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
