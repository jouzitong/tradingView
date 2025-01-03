package ai.zzt.okx.storage.utils;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhouzhitong
 * @since 2024/6/23
 **/
public class ByteBufferPool {

    private final ConcurrentLinkedQueue<ByteBuffer> pool;
    private final int bufferSize;

    public ByteBufferPool(int poolSize, int bufferSize) {
        this.pool = new ConcurrentLinkedQueue<>();
        this.bufferSize = bufferSize;

        for (int i = 0; i < poolSize; i++) {
            pool.add(ByteBuffer.allocate(bufferSize));
        }
    }

    public ByteBuffer get() {
        ByteBuffer buffer = pool.poll();
        if (buffer == null) {
            buffer = ByteBuffer.allocate(bufferSize);
        }
        return buffer;
    }

    public void clear(ByteBuffer buffer) {
        buffer.flip();
        buffer.clear();
        pool.offer(buffer);
    }

}
