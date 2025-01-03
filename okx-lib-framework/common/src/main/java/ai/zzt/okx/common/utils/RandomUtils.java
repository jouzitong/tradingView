package ai.zzt.okx.common.utils;

import java.util.Random;

/**
 * @author zhouzhitong
 * @since 2024/9/9
 **/
public class RandomUtils {

    // 定义可用于生成随机字符串的字符集
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 16; // 随机字符串的长度

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        String randomString = generateRandomString(STRING_LENGTH);
        System.out.println("随机字符串: " + randomString);
    }

    public static Integer randomInt() {
        return randomInt(0, 100);
    }

    public static Integer randomInt(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static String generateRandomString(int length) {
        // 获取当前时间的纳秒值
        long nanoTime = System.nanoTime();

        // 将纳秒时间作为种子传递给随机数生成器
        Random random = new Random(nanoTime);

        StringBuilder sb = new StringBuilder(length);

        // 生成随机字符串
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

}
