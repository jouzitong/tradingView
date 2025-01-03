package ai.zzt.okx.storage.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * K 线数据消费者
 *
 * @author wyz
 */
@AllArgsConstructor
@Data
public class LocalCustomer implements Customer<MarkPrice> {

    /**
     * 结束标记
     */
    private volatile AtomicBoolean end;

    /**
     * 数据总量
     */
    private long count = 0;

    /**
     * 数据队列
     */
    private volatile LinkedBlockingQueue<MarkPrice> dataQueue;

    public LocalCustomer() {
        end = new AtomicBoolean(false);
        dataQueue = new LinkedBlockingQueue<>(10000);
    }

    /**
     * 结束消费
     */
    @Override
    public void end() {
        end.set(true);
    }

    /**
     * 是否结束消费
     *
     * @return true
     */
    @Override
    public boolean isEnd() {
        return end.get() && isEmpty();
    }

    @Override
    public void close() {
        end();
    }

    @Override
    public void commit(MarkPrice mp) {
        // TODO commit 作用, 将 MarkPrice 对象池话, 但是没有实现
    }

    @Override
    public void commit(List<MarkPrice> mps) {

    }

    /**
     * 获取数据
     *
     * @return 数据
     * @throws InterruptedException 线程中断异常
     */
    @Override
    public MarkPrice take() throws InterruptedException {
        return dataQueue.take();
    }

    @Override
    public List<MarkPrice> takeAll(int limit) throws InterruptedException {
        if (isEnd()) {
            return Collections.EMPTY_LIST;
        }
        List<MarkPrice> mps = new ArrayList<>();
        while (!dataQueue.isEmpty() && mps.size() < limit) {
            mps.add(dataQueue.take());
        }
        return mps;
    }

    /**
     * 放入数据
     *
     * @param data 数据
     */
    @Override
    public void put(MarkPrice data) throws InterruptedException {
        count++;
        dataQueue.put(data);
    }

    /**
     * 创建空消费者
     *
     * @return 空消费者
     */
    public static LocalCustomer empty() {
        LocalCustomer kLineCustomer = new LocalCustomer();
        kLineCustomer.end();
        return kLineCustomer;
    }

    @Override
    public boolean isEmpty() {
        return dataQueue.isEmpty();
    }

    @Override
    public long total() {
        return count;
    }

    @Override
    public int size() {
        return dataQueue.size();
    }
}
