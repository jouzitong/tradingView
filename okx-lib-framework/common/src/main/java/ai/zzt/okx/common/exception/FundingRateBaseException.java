package ai.zzt.okx.common.exception;

import ai.zzt.okx.common.exception.base.BaseException;

/**
 * @author zhouzhitong
 * @since 2024/5/30
 **/
public class FundingRateBaseException extends BaseException {

    public final static ErrCode ERROR = new ErrCode(10000, "未完成的功能异常");

    public FundingRateBaseException(int code, String msg) {
        super(code, msg);
    }

    public FundingRateBaseException(ErrCode code) {
        super(code);
    }
}
