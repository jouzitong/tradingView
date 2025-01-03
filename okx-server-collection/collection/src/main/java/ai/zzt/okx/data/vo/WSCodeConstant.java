package ai.zzt.okx.data.vo;

/**
 * code 常量
 *
 * @author zhouzhitong
 * @since 2024/7/16
 **/
public interface WSCodeConstant {

    String PING = "ping";

    String PONG = "pong";

    /**
     * 成功
     */
    int SUCCESS = 0;

    /**
     * 失败
     */
    int FAIL = -1;

    /**
     * 【请求】ws 创建请求
     */
    int WS_CREATE_REQUEST = 1_001;

    /**
     * 【请求】ws k线数据请求全量数据
     */
    int WS_K_LINE_REQUEST_ALL = 1_101;

    /**
     * 【请求】ws k线数据订阅
     */
    int WS_K_LINE_REQUEST_SUB = 1_102;

    /**
     * 【响应-推送】ws k线数据推送
     */
    int WS_K_LINE_RESPONSE_PUSH = 3_102;

}
