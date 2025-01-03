package ai.zzt.okx.common.exception;

import ai.zzt.okx.common.context.SystemContext;

/**
 * @author zhouzhitong
 * @since 2024/5/10
 **/
public class SystemRuntimeException extends RuntimeException{

    public SystemRuntimeException(String message) {
        super(message);
        SystemContext.close();
    }
}
