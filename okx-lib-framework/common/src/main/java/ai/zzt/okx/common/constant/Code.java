package ai.zzt.okx.common.constant;

/**
 * 状态码
 *
 * @author zhouzhitong
 * @since 2024/7/30
 **/
public record Code(int code, String msg) {

    public static final Code PING = new Code(9999, "ping");

    public static final Code PONG = new Code(9999, "pong");
    /**
     * 成功
     */
    public static final Code SUCCESS = new Code(0, "success");

    /**
     * 失败
     */
    public static final Code FAIL = new Code(1, "fail");

}
