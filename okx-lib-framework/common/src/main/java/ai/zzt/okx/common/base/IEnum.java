package ai.zzt.okx.common.base;

/**
 * @author zhouzhitong
 * @since 2024/7/17
 **/
public interface IEnum {

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    // TODO code 不一定是 int
    int getCode();

    /**
     * 获取枚举描述
     *
     * @return 枚举描述
     */
    String getName();

    /**
     * 当前枚举值是否可用
     *
     * @return 是否可用
     */
    default boolean isEnable() {
        return true;
    }

    default String toStr() {
        return getName();
    }

}
