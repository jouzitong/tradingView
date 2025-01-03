package ai.zzt.okx.v5.entity.rest;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OkxRestError implements IOkxRestObject<Void> {

    @JSONField(name = "code")
    @JsonProperty("code")
    private int code;

    @JSONField(name = "msg")
    @JsonProperty("msg")
    private String msg;

    @Override
    public Void getData() {
        return null;
    }
}
