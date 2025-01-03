package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.api.BaseApiTest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.MgnMode;

import java.util.List;

/**
 *
 * @author Jou Ziton
 * @since 2024/6/4
 **/
public class AccountApiTest extends BaseApiTest {

    private final AccountApi accountApi = createOkxOrderAlgoApi(AccountApi.class);

    @Test
    public void getPendingAlgoOrders() {
        Single<R<AlgoOrder>> pendingAlgoOrders = accountApi.getPendingAlgoOrders(OrdType.OCO.value(), InstrumentType.SWAP.value());
        R<AlgoOrder> test = test(pendingAlgoOrders);
        System.out.println(test);
    }

    @Test
    public void getBalance() {
        Single<R<Balance>> usdt = accountApi.getBalance(null);
        R<Balance> balanceR = usdt.blockingGet();
        System.out.println(balanceR);
    }

    @Test
    public void getPositions() {
        Single<R<Positions>> res = accountApi.getPositions();
        R<Positions> positionsR = res.blockingGet();
        for (Positions positions : positionsR.getData()) {
            System.out.println(positions);
            System.out.println("------");
        }
    }

    @Test
    public void getLeverageInfo() {
        Single<R<LeverageResp>> leverageInfo = accountApi.getLeverageInfo("BTC-USDT-SWAP", null, MgnMode.CROSS.getValue());
        R<LeverageResp> res = OkxRestApiFactory.get(leverageInfo);
        List<LeverageResp> data = res.getData();
        System.out.println(data);
    }

}
