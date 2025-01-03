package ai.zzt.okx.emulator.callBack;

import ai.zzt.okx.calculate.context.StatisticsContext;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.emulator.dto.TaskSettings;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/5/23
 **/
@Data
@NoArgsConstructor
public class CustomerTaskDTO {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务组ID
     */
    private String groupId;

    /**
     * 开始时间. 默认从头开始
     * <p>
     * 说明: 消息有很多, 指定读取从哪个时间戳的消息开始读取. 默认为创建任务时的当前时间
     */
    private long startTime;

    /**
     * 结束时间. 默认到现在为止
     * <p>
     * 说明: 消息有很多, 指定读取到哪个时间戳的消息就停止读取. 默认为创建任务时的当前时间
     */
    private long endTime;

    /**
     * 配置上下文
     */
    private SettingsContext settingsContext;

    /**
     * 任务设置
     */
    private TaskSettings taskSettings = new TaskSettings();

    /**
     * 时间粒度. 默认 分钟
     */
    private Bar bar = Bar.M_1;

    public CustomerTaskDTO(String instId, String taskId) {
        this(instId, taskId, SystemContext.currentTimeMillis());
    }

    public CustomerTaskDTO(String instId, String taskId, long endTime) {
        this.instId = instId;
        this.taskId = taskId;
        this.endTime = endTime;
    }

    /**
     * 创建TradeOrderRequest对象. 请保证一个任务只能创建一个 TradeOrderRequest 对象.
     *
     * @return TradeOrderRequest
     */
    public TradeOrderRequest buildRequest() {
        TradeOrderRequest request;
        request = new TradeOrderRequest();
        request.setInstId(instId);
        request.setSettingsContext(settingsContext);
        FundingRate fundingRate = new FundingRate();
        fundingRate.setFundingRate(taskSettings.getFeeRate());
        request.setFundingRate(fundingRate);

        request.setStatisticsContext(new StatisticsContext());
        request.setAccountContext(newAccountContext(taskSettings.getInitCash()));
        request.setSessionId(taskId);
        return request;
    }

    private AccountContext newAccountContext(BigDecimal initCash) {
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
