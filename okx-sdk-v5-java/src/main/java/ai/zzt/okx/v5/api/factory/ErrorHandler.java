package ai.zzt.okx.v5.api.factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ai.zzt.okx.v5.entity.rest.IOkxRestObject;

/**
 * 在请求出现异常之后, 可以通过实现该接口来处理异常
 * <p>
 * 异常不局限于抛出程序异常, 还有错误码, 错误信息等
 *
 * @author Jou Ziton
 * @since 2024/6/1
 **/
public interface ErrorHandler {

    /**
     * 处理的错误码
     * <p>
     * <a href="https://www.okx.com/docs-v5/zh/#error-code">Okx 错误码参考</a>
     *
     * @return 错误码
     */
    int code();

    /**
     * 具体处理逻辑
     *
     * @param res 响应结果
     * @param e   抛出的异常 TODO 目前还没想好怎么用
     * @return 是否处理成功
     */
    boolean handler(@NotNull IOkxRestObject<?> res, @Nullable Throwable e);

}
