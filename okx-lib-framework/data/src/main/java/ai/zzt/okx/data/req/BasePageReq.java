package ai.zzt.okx.data.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouzhitong
 * @since 2024/10/2
 **/
@Data
@NoArgsConstructor
public class BasePageReq {

    private int page;

    private int size;

    public BasePageReq(int page, int size) {
        this.page = page;
        this.size = size;
    }

}
