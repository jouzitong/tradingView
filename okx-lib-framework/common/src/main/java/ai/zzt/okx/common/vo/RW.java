package ai.zzt.okx.common.vo;

import ai.zzt.okx.common.constant.Code;
import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.base.IR;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ws 接口请求和响应包装类
 *
 * @author zhouzhitong
 * @since 2024/7/16
 **/
@Data
@ToString
@NoArgsConstructor
public class RW<T> implements IR<T> {

    /**
     * 结果代码，0表示成功
     */
    @JSONField(name = "code")
    @JsonProperty("code")
    private int code = Code.SUCCESS.code();

    /**
     * 操作码
     */
    @JSONField(name = "opCode")
    @JsonProperty("opCode")
    private WsOpCode opCode;

    /**
     * 描述信息
     */
    @JSONField(name = "msg")
    @JsonProperty("msg")
    private String msg;

    @JSONField(name = "data")
    @JsonProperty("data")
    private T data;

    /**
     * REST网关接收请求时的时间戳，Unix时间戳的微秒数格式，如 1597026383085123
     * 返回的时间是请求验证后的时间。
     */
    @JSONField(name = "inTime")
    @JsonProperty("inTime")
    private Long inTime;

    /**
     * REST网关发送响应时的时间戳，Unix时间戳的微秒数格式，如 1597026383085123
     */
    @JSONField(name = "outTime")
    @JsonProperty("outTime")
    private Long outTime;

    public RW(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RW(T data) {
        this.data = data;
    }

    public RW(WsOpCode opCode, T data) {
        this.opCode = opCode;
        this.data = data;
    }

    public RW(int code) {
        this.code = code;
    }

    public void setArgs(T data) {
        this.data = data;
    }

    public static RW<Void> ok() {
        return new RW<>(Code.SUCCESS.code());
    }

    /**
     * 成功返回
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 成功响应
     */
    public static <T> RW<T> ok(T data) {
        return new RW<>(data);
    }

    /**
     * 成功返回
     *
     * @param opCode 操作码
     * @param data   响应数据
     * @param <T>    响应数据类型
     * @return 成功响应
     */
    public static <T> RW<T> ok(WsOpCode opCode, T data) {
        return new RW<>(opCode, data);
    }

    public static RW<Void> fail() {
        return new RW<>(Code.FAIL.code(), Code.FAIL.msg());
    }

    public static RW<Void> of(int code) {
        return new RW<>(code);
    }

    public static RW<Void> of(int code, String msg) {
        return new RW<>(code, msg);
    }

}
