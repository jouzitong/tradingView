package ai.zzt.okx.dispatcher.filter.open;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

/**
 * 合约开仓过滤器
 *
 * @author zhouzhitong
 * @since 2024-08-29
 **/
@Service
@Slf4j
public class SwapOpenOrderFilter extends BaseOpenOrderFilter {

    @Override
    protected InstrumentType type() {
        return InstrumentType.SWAP;
    }
}
