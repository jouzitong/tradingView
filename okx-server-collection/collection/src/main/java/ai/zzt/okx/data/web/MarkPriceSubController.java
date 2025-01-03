package ai.zzt.okx.data.web;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@RestController
@RequestMapping("/api/v1/mark-prices")
public class MarkPriceSubController {

    @Resource
    private OkxWsApiServiceWrapper okxWsApiServiceWrapper;

    @GetMapping("/sub/{id}")
    public R<Void> subInst(@PathVariable String id) {
        okxWsApiServiceWrapper.subMarkPrice(InstUtils.getSwapId(id));
        return R.ok();
    }

    @GetMapping("/unsub/{id}")
    public R<Void> unsubInst(@PathVariable String id) {
        okxWsApiServiceWrapper.unsubMarkPrice(InstUtils.getSwapId(id));
        return R.ok();
    }

    /**
     * 获取通道状态
     *
     * @return true: 连接正常; false, 连接一查过
     */
    @GetMapping("/channel/state/public")
    public R<Boolean> getPublicChannelState() {
        boolean connected = okxWsApiServiceWrapper.isConnected(WsChannel.PUBLIC);
        return R.ok(connected);
    }

    /**
     * public 通道重新连接
     */
    @PostMapping("/channel/reconnect/public")
    public R<Void> reconnectPublicChannel() {
        okxWsApiServiceWrapper.manualReconnect(WsChannel.PUBLIC);
        return R.ok();
    }

    @GetMapping("/subInstList")
    public R<List<String>> getSubInstList() {
        return R.ok(okxWsApiServiceWrapper.getSubMpList());
    }

}
