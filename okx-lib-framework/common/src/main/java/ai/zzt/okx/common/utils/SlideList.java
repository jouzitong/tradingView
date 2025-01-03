package ai.zzt.okx.common.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 自定义滑动窗口. 用数组实现，支持迭代器遍历.
 * <p>
 * TODO 应该迁移到工具类中
 *
 * @param <T>
 */
public class SlideList<T> implements Iterable<T> {
    private final T[] array;
    private int size;
    private int head;
    private int tail;

    @Getter
    private long count = 0;

    public SlideList(int capacity) {
        array = (T[]) new Object[capacity];
        size = 0;
        head = 0;
        tail = 0;
    }

    public void add(T item) {
        if (item instanceof BigDecimal b && b.scale() > 20) {
            item = (T) b.setScale(12, RoundingMode.HALF_UP);
        }
        if (size == array.length) {
            head = (head + 1) % array.length; // Move head to next position
        } else {
            size++;
        }
        array[tail] = item; // Add new item at tail
        tail = (tail + 1) % array.length; // Move tail to next position
        count++; // Increase count
    }

    /**
     * 更新最后一个元素
     *
     * @param item 数据
     */
    public void setLast(T item) {
        if (isEmpty()) {
            throw new NoSuchElementException("CircularArray is empty");
        }
        if (item instanceof BigDecimal b && b.scale() > 20) {
            item = (T) b.setScale(12, RoundingMode.HALF_UP);
        }
        array[(tail - 1 + array.length) % array.length] = item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index [" + index + "] out of bounds, and size is" + size);
        }
        return array[(head + index) % array.length];
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("CircularArray is empty");
        }
        return array[head];
    }

    public T getLast() {
        if (isEmpty()) {
//            throw new NoSuchElementException("CircularArray is empty");
            return null;
        }
        return array[(tail - 1 + array.length) % array.length];
    }

    /**
     * 获取倒数第二个元素
     *
     * @return 倒数第二个元素
     */
    public T getSecLast() {
        if (size < 2) {
            throw new NoSuchElementException("CircularArray size is less than 2");
        }
        return array[(tail - 2 + array.length) % array.length];
    }

    public boolean isFull() {
        return size == array.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public long count() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularArrayIterator();
    }

    private class CircularArrayIterator implements Iterator<T> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(index++);
        }
    }

    public static <T> SlideList<T> empty() {
        return new SlideList<>(0);
    }

    /**
     * 返回顺序的数组
     *
     * @return 顺序的数组
     */
    @JsonValue
    public T[] toArray() {
        T[] result = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = get(i);
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public void clear() {
        size = 0;
        head = 0;
        tail = 0;
    }

    public static void main(String[] args) {
        SlideList<Integer> slideList = new SlideList<>(5);
        for (int i = 0; i < 13; i++) {
            slideList.add(i);
        }

        System.out.println("First element: " + slideList.getFirst());
        System.out.println("Last element: " + slideList.getLast());

        slideList.setLast(10);
        System.out.println("Last element after setLast: " + slideList.getLast());

        // Iterate over elements
        System.out.println("Elements:");
        for (Integer item : slideList) {
            System.out.print(item + " "); // Output: 1 2 3 4 5
        }
    }
}
