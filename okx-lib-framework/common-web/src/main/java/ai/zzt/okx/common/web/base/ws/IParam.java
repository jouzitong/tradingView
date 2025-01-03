package ai.zzt.okx.common.web.base.ws;

import ai.zzt.okx.common.constant.WsOpCode;

import java.util.List;

/**
 * 通用参数接口.
 * <p>
 * 目前主要用于定义 websocket通信参数.
 *
 * @author zhouzhitong
 * @since 2024/10/30
 **/
public interface IParam<T> {

    /**
     * 设置操作码, 主要是 websocket通信需要的
     *
     * @param opCode 操作码
     */
    void setOpCode(WsOpCode opCode);

    /**
     * 设置操作描述信息
     *
     * @param msg 操作描述信息
     */
    void setMsg(String msg);

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    void setSt(long startTime);

    /**
     * 设置参数
     *
     * @param args 参数
     */
    void setArgs(List<T> args);

    /**
     * TODO 设置签名
     *
     * @param sign 签名
     */
    void setSign(String sign);

    WsOpCode getOpCode();

    String getMsg();

    long getSt();

    List<T> getArgs();

    String getSign();

}
