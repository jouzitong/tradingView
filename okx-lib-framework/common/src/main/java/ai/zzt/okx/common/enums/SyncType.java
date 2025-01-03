package ai.zzt.okx.common.enums;

import ai.zzt.okx.common.base.IEnum;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/7/24
 **/
@Getter
public enum SyncType implements IEnum {

    MARKET_DATA(1, "market_data"),
    ORDER(2, "order"),
    STORE_DATA(3, "store_data"),

    POSITIONS(7, "positions"),
    ;
    private final int code;

    private final String name;


    SyncType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
