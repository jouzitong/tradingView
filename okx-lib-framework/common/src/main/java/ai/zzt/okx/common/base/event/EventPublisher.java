package ai.zzt.okx.common.base.event;

/**
 * @author zhouzhitong
 * @since 2024/5/19
 **/
public interface EventPublisher {

    /**
     * 发布一个事件
     *
     * @param event 事件
     */
    void publish(Event event);

}
