package ai.zzt.okx.emulator.utils.creator;

import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.v5.entity.rest.account.Balance;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/26
 **/
public class AccountUtils {

    public static AccountContext newAccountContext() {
        return newAccountContext(BigDecimal.valueOf(100));
    }

    public static AccountContext newAccountContext(BigDecimal initCash) {
        AccountContext accountContext = new AccountContext();

        Balance balance = new Balance();
        balance.setNotionalUsd(BigDecimal.ZERO);
        balance.setTotalEq(initCash);
        balance.setUTime(System.currentTimeMillis());
        balance.setDetails(List.of());
        accountContext.setBalance(balance);

        return accountContext;
    }

}
