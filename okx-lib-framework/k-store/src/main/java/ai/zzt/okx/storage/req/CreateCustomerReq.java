package ai.zzt.okx.storage.req;

import ai.zzt.okx.v5.enumeration.Bar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerReq {

    private Bar bar;

    private String instId;

    private long st;

    private long et;

}
