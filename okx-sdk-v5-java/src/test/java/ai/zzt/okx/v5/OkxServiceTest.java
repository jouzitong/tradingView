package ai.zzt.okx.v5;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OkxServiceTest {

//    private OkxApiService getOkxService() {
//        // 模拟盘
//        String apiKey = System.getenv("API_KEY");
//        String passphrase = System.getenv("PASSPHRASE");
//        String secretKey = System.getenv("SECRET_KEY");
//        return new OkxApiService(apiKey, secretKey, passphrase, true);
//    }
//
//    @Test
//    public void testGET() {
//        OkxApiService service = getOkxService();
//        final var accountBalanceOkxRestResponse = service.getBalance("BTC");
//        log.info("test: {}", accountBalanceOkxRestResponse);
//    }
//
//    @Test
//    public void placeOrder() {
//        OkxApiService service = getOkxService();
//        var response = service.placeOrder(PlaceOrderReq.builder()
//                .instId("BTC-USDT")
//                .tdMode(TdMode.ISOLATED)
//                .side(Side.BUY)
//                .ordType(OrderType.MARKET)
//                .sz(BigDecimal.ONE)
//                .build());
//
////        response = service.cancelOrder(CancelOrderReq.builder()
////                .instId("BTC-USDT")
////                .ordId("hell")
////                .build());
//
//
//        log.info("test: {}", response);
//    }
//
//    @Test
//    public void testIssue2() throws JsonProcessingException {
//        ObjectMapper objectMapper = defaultObjectMapper();
//        Order order = objectMapper.readValue("{\"tpTriggerPxType\": \"a\"}", Order.class);
//        System.out.println(order);
//    }
//
//    public static ObjectMapper defaultObjectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        return mapper;
//    }

}
