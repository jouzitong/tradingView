package ai.zzt.okx.common.exception;

import ai.zzt.okx.common.exception.base.BaseRuntimeException;

/**
 * 未完成的功能异常
 * <p>
 * TODO 应该要提示具体什么功能没完成
 *
 * @author zhouzhitong
 * @since 2024/5/30
 **/
public class TodoRuntimeException extends BaseRuntimeException {

    private final static ErrCode TODO = new ErrCode(10000, "未完成的功能异常");

    public TodoRuntimeException(String msg) {
        super(TODO.code(), msg);
    }

    public TodoRuntimeException() {
        super(TODO);
    }

    public TodoRuntimeException(int code, String msg) {
        super(code, msg);
    }

    public TodoRuntimeException(ErrCode code) {
        super(code);
    }

}
