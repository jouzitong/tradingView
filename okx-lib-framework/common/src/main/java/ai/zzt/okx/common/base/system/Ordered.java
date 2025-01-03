package ai.zzt.okx.common.base.system;

/**
 * 接口执行顺序
 *
 * @author zhouzhitong
 * @since 2024/6/3
 **/
public interface Ordered {

    int HIGH = Integer.MIN_VALUE + 1000;

    int LOW = Integer.MAX_VALUE - 1000;

    /**
     * 执行循序. 这个要参考已有实现的接口顺序
     *
     * @return 顺序. 值越小, 越先执行
     */
    default int order() {
        return 0;
    }

}
