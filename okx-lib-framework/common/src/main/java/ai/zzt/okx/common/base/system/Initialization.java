package ai.zzt.okx.common.base.system;

/**
 * 系统初始化接口, 建议每个模块都实现这个接口. 这个接口不影响系统正常运行, 即使没有实现, 也不应该影响系统正常运行
 *
 * <pre>
 * 接口展望:
 * 1. 初始化组件是需要顺序的, 每个顺序之间尽可能空间隔开, 避免互相影响
 * 2. 这个初始化, 不是系统启动必要的. 也就是说, 我可以不初始化, 系统也能正常运行. 例如, 初始化ws连接, 我不需要用到ws连接, 所以我可以不初始化
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/5/29
 **/
public interface Initialization extends Ordered {

    /**
     * 系统启动后调用的. 初始化方法
     */
    void initialize();

}
