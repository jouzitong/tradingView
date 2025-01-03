package ai.zzt.okx.v5.enumeration;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
public enum State {

    /**
     * 交易中
     */
    LIVE("live"),
    /**
     * 暂停中
     */
    SUSPEND("suspend"),

    /**
     * 预上线，交割和期权合约轮转生成到开始交易；部分交易产品上线前
     */
    PRE_OPEN("preopen"),

    /**
     * 测试中（测试产品，不可交易）
     */
    TEST("test"),

    ;



    private final String value;


    State(String value) {
        this.value = value;
    }
}
