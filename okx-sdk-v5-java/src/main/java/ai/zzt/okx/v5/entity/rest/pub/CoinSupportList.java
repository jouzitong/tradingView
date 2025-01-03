package ai.zzt.okx.v5.entity.rest.pub;

import lombok.Data;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;

import java.util.Set;

/**
 * @author zhouzhitong
 * @since 2024/4/29
 **/
@Data
public class CoinSupportList implements IOkxRestRsp {

    /**
     * 合约交易大数据接口功能支持的币种
     */
    private Set<String> contract;

    /**
     * 期权交易大数据接口功能支持的币种【目前不玩】
     */
    @Deprecated
    private Set<String> option;

    /**
     * 现货交易大数据接口功能支持的币种
     */
    @Deprecated
    private Set<String> spot;

}
