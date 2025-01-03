package ai.zzt.okx.data.ws.resp;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Data
public class WsResp {

    private int code;

    private String msg;

    private List<Object> data;

    public void add(Object data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        if (data instanceof List<?> list) {
            this.data.addAll(list);
        } else {
            this.data.add(data);
        }
    }

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

}
