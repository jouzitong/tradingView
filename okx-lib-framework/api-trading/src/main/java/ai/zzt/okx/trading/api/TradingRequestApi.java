package ai.zzt.okx.trading.api;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.trading.dto.InstDetailDTO;
import ai.zzt.okx.trading.dto.InstInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@FeignClient(name = "trading-api", url = "${app.trading.url}" + "/api/v1/trading")
public interface TradingRequestApi {

    /**
     * 获取请求上下文
     *
     * @param instId 产品ID
     * @return 请求上下文
     */
    @GetMapping("/{instId}")
    TradeOrderRequest get(@PathVariable("instId") String instId);

    /**
     * 获取配置上下文
     *
     * @param instId 产品ID
     * @return 配置上下文
     */
    @GetMapping("/settings/{instId}")
    R<SettingsContext> getSettings(@PathVariable("instId") String instId);

    /**
     * 更新能否自动下单
     *
     * @param instId           产品ID
     * @param key              api-Key
     * @param enablePlaceOrder 能否自动下单
     * @return 更新成功
     */
    @PostMapping("/enablePlaceOrder/{instId}/{enable}")
    R<Void> updatePlaceOrder(@PathVariable("instId") String instId,
                             @RequestParam("key") String key,
                             @PathVariable("enable") Boolean enablePlaceOrder);

    /**
     * 同步更新配置
     *
     * @param instId 产品ID
     * @param key    api-key
     */
    @PutMapping("/{instId}")
    R<Void> update(@PathVariable("instId") String instId,
                   @RequestParam("key") String key);


    /**
     * 新增量化交易币种
     *
     * @param instId 产品ID
     * @param key    api-key
     */
    @PostMapping("/{instId}")
    R<Void> add(@PathVariable("instId") String instId,
                @RequestParam("key") String key);

    /**
     * 删除量化交易币种
     *
     * @param instId 产品ID
     * @param key    api-key
     */
    @DeleteMapping("/{instId}")
    R<Void> delete(@PathVariable("instId") String instId,
                   @RequestParam("key") String key);

    /**
     * 运行产品列表
     *
     * @return 产品列表
     */
    @GetMapping("/list")
    R<List<InstInfoDTO>> list();

    /**
     * 获取每个产品的趋势详情
     *
     * @param instId 产品ID
     * @return
     */
    @GetMapping("/trendDetail/{instId}")
    R<InstDetailDTO> getTrendDetail(@PathVariable("instId") String instId);

}
