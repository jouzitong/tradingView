package ai.zzt.okx.common.exception.base;

import ai.zzt.okx.common.exception.ErrCode;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/5/30
 **/
@Getter
public class BaseRuntimeException extends RuntimeException {

    private final ErrCode code;

    public BaseRuntimeException(int code, String message) {
        super(message);
        this.code = new ErrCode(code, message);
    }

    public BaseRuntimeException(ErrCode code) {
        super(code.message());
        this.code = code;
    }

}
