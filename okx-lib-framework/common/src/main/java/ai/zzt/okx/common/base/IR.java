package ai.zzt.okx.common.base;

import ai.zzt.okx.common.constant.WsOpCode;

/**
 * 通用返回接口
 *
 * @author zhouzhitong
 * @since 2024/7/16
 **/
public interface IR<T> {

    /**
     * 返回状态码, 0表示成功, 其他表示失败
     *
     * @return 状态码
     */
    int getCode();

    /**
     * 返回操作码, 主要是 websocket通信需要的
     *
     * @return 操作码
     */
    default WsOpCode getOpCode() {
        return null;
    }

    /**
     * 描述信息. 如果有操作码, 则是对操作码的描述. 如果操作码没有值, 则描述状态的的信息
     *
     * @return 描述信息
     */
    String getMsg();

    /**
     * 返回数据
     *
     * @return 数据
     */
    T getData();

    /**
     * 获取签名
     *
     * @return 签名
     */
    default String getSign() {
        return null;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    default int getStatus() {
        return getCode() == 0 ? 200 : 500;
    }

}
