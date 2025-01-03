package ai.zzt.okx.data.ws.request;

import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.web.base.ws.IParam;
import lombok.Data;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Data
public class PublicParam<T> implements IParam<T> {

    private WsOpCode opCode;

    private String msg;

    private long st;

    private List<T> args;

    private String sign;

}
