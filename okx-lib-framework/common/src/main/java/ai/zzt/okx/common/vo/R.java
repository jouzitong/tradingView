package ai.zzt.okx.common.vo;

import ai.zzt.okx.common.constant.Code;
import ai.zzt.okx.common.base.IR;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * rest 接口请求和响应包装类
 *
 * @author zhouzhitong
 * @since 2024/7/16
 **/
@Data
@ToString
@NoArgsConstructor
public class R<T> implements IR<T> {

    /**
     * 结果代码，0表示成功
     */
    @JSONField(name = "code")
    @JsonProperty("code")
    private int code = Code.SUCCESS.code();

    /**
     * 描述信息
     */
    @JSONField(name = "msg")
    @JsonProperty("msg")
    private String msg;

    @JSONField(name = "data")
    @JsonProperty("data")
    private T data;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(T data) {
        this.data = data;
    }

    public R(int code) {
        this.code = code;
    }

    @JsonIgnore
    public boolean isOk() {
        return this.code == Code.SUCCESS.code();
    }

    public static R<Void> ok() {
        return new R<>(Code.SUCCESS.code());
    }

    /**
     * 成功返回
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 成功响应
     */
    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public static R<Void> fail() {
        return new R<>(Code.FAIL.code(), Code.FAIL.msg());
    }

    public static R<Void> fail(String msg) {
        return new R<>(Code.FAIL.code(), msg);
    }

    public static R<Void> of(int code) {
        return new R<>(code);
    }

    public static R<Void> of(int code, String msg) {
        return new R<>(code, msg);
    }

}
