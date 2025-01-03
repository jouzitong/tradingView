package ai.zzt.okx.common.base.scheduled;

/**
 * 定时任务服务
 *
 * @author zhouzhitong
 * @since 2024/10/21
 **/
public interface ScheduledService {

    void scheduled();

    void run();

}
