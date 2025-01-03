package ai.zzt.okx.common.exception;

/**
 * 异常描述类.
 * <p>
 * code 应该是唯一的, 并进行统一管理
 *
 * @author zhouzhitong
 * @since 2024/5/30
 **/
public record ErrCode(int code, String message) {
}
