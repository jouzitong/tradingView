package ai.zzt.okx.v5.entity.ws.pub;

import lombok.Data;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;

/**
 * @author zhouzhitong
 * @since 2024/5/15
 **/
@Data
public class SystemTime implements IOkxRestRsp {

    private long ts;

}
