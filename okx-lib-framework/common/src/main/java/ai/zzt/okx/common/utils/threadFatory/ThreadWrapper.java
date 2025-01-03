package ai.zzt.okx.common.utils.threadFatory;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
public class ThreadWrapper extends Thread {

    public ThreadWrapper(Runnable task, String name) {
        super(task, name);
    }

    public ThreadWrapper(Runnable task) {
        super(task);
    }
}
