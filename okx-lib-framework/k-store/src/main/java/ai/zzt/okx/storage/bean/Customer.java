package ai.zzt.okx.storage.bean;

import java.io.Closeable;
import java.util.List;

/**
 * 消费者
 * <pre>
 * 消费流程:
 * 1. 创建一个消费者.
 * 2. 生产者通过{@link #put(Object)} 方法添加数据
 * 3. 调用{@link Customer#take() take()} 或者 {@link Customer#takeAll()} 等指令获取数据. 这个获取过程是阻塞的
 * 4. 消费者消费一批后, 应该调用{@link #commit(Object)}.
 * 5. 当消费者不需要再消费、或生产者不再生产消息、或出现异常时, 应该调用{@link #end()}
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/6/23
 **/
public interface Customer<B> extends Closeable {

    /**
     * 获取一条数据. 如果没有数据, 则阻塞等待
     *
     * @return 一条数据
     */
    B take() throws InterruptedException;

    /**
     * 获取所有数据. 如果没有数据, 则阻塞等待
     *
     * @return 所有数据(限制 500 条)
     */
    default List<B> takeAll() throws InterruptedException {
        return takeAll(500);
    }

    /**
     * 获取所有数据
     *
     * @param limit 限制条数
     * @return
     */
    List<B> takeAll(int limit) throws InterruptedException;

    /**
     * 增加一条数据
     *
     * @param b 一条数据
     */
    void put(B b) throws InterruptedException;

    /**
     * 增加一组数据
     *
     * @param dataList 一组数据
     */
    default void putAll(List<B> dataList) throws InterruptedException {
        for (B data : dataList) {
            put(data);
        }
    }

    /**
     * 提交数据
     *
     * @param data 数据
     */
    void commit(B data);

    /**
     * 提交一组数据
     *
     * @param dataList 数据
     */
    void commit(List<B> dataList);

    /**
     * 是否结束消费
     *
     * @return true 结束消费
     */
    boolean isEnd();

    /**
     * 是否为空
     *
     * @return true
     */
    boolean isEmpty();

    /**
     * 结束消费
     */
    void end();

    /**
     * 总数
     *
     * @return 总数
     */
    long total();

    /**
     * 当前数量
     *
     * @return 当前数量
     */
    int size();

}
