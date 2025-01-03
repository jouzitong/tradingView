//package ai.zzt.okx.emulator.web;
//
//import ai.zzt.okx.common.utils.ThreadUtils;
//import ai.zzt.okx.common.vo.R;
//import ai.zzt.okx.okx_client.serivce.OkxRestApiService;
//import ai.zzt.okx.okx_client.serivce.rest.KLineCollectionService;
//import ai.zzt.okx.v5.enumeration.Bar;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * K 线数据收集
// * <p>
// * TODO 临时放这里, 后期应该找个合适的地方
// *
// * @author zhouzhitong
// * @since 2024/9/24
// **/
//@RestController
//@RequestMapping("/api/v1/kline/history")
//public class KLineHistoryController {
//
//    @Resource
//    private OkxRestApiService okxRestApiService;
//
//    @Resource
//    private KLineCollectionService kLineCollectionService;
//
//    @Deprecated
//    @GetMapping("/{instId}")
//    public void storeKData(@PathVariable String instId) {
//        ThreadUtils.runIO(() -> {
//            kLineCollectionService.collectHistoryData(instId, Bar.M_1);
//        });
//    }
//
//    @GetMapping("start")
//    public R<Void> start() {
//        kLineCollectionService.collectHistoryData();
//        return R.ok();
//    }
//
//}
