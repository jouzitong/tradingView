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
public class DeprecatedRuntimeException extends BaseRuntimeException {

    private final static ErrCode DEPRECATED = new ErrCode(10001, "弃用异常, 请勿使用");

    public DeprecatedRuntimeException() {
        super(DEPRECATED);
    }

    public DeprecatedRuntimeException(int code, String msg) {
        super(code, msg);
    }

    public DeprecatedRuntimeException(ErrCode code) {
        super(code);
    }

}
