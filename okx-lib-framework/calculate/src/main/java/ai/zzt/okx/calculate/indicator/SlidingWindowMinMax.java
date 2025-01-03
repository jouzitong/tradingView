package ai.zzt.okx.calculate.indicator;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author zhouzhitong
 * @since 2024/9/4
 **/
public class SlidingWindowMinMax {
    private Deque<Integer> maxDeque;
    private Deque<Integer> minDeque;
    private int[] nums;
    private int k;

    public SlidingWindowMinMax(int[] nums, int k) {
        this.nums = nums;
        this.k = k;
        this.maxDeque = new LinkedList<>();
        this.minDeque = new LinkedList<>();
    }

    public void process() {
        for (int i = 0; i < nums.length; i++) {
            // 移除不在窗口内的元素
            if (!maxDeque.isEmpty() && maxDeque.peekFirst() < i - k + 1) {
                maxDeque.pollFirst();
            }
            if (!minDeque.isEmpty() && minDeque.peekFirst() < i - k + 1) {
                minDeque.pollFirst();
            }

            // 保持双端队列的递减/递增特性
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[i]) {
                maxDeque.pollLast();
            }
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[i]) {
                minDeque.pollLast();
            }

            // 将当前元素的索引加入队列
            maxDeque.offerLast(i);
            minDeque.offerLast(i);

            // 当窗口形成时输出结果
            if (i >= k - 1) {
                System.out.println("Window [" + (i - k + 1) + ", " + i + "]: Max = " + nums[maxDeque.peekFirst()] + ", Min = " + nums[minDeque.peekFirst()]);
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        SlidingWindowMinMax window = new SlidingWindowMinMax(nums, k);
        window.process();
    }
}
