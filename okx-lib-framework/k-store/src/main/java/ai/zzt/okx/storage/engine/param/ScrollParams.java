package ai.zzt.okx.storage.engine.param;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;

/**
 * @author zhouzhitong
 * @since 2024/11/2
 **/
@Data
public class ScrollParams {

    /**
     * 实例ID
     */
    private String instId;

    /**
     * 时间周期
     */
    private Bar bar;

    /**
     * 数据条数
     */
    private int size = 1000;

    /**
     * 第几页, 从 1 开始
     */
    private int page = 1;

    /**
     * 开始时间
     */
    private Long st;

    /**
     * 结束时间
     */
    private Long et;

    public ScrollParams() {
    }

    public long getEt() {
        if (et != null) return et;
        if (bar == null) throw new TodoRuntimeException();

        long scale = bar.getScale();
        long now = SystemContext.currentTimeMillis();
        return this.et = now - (page - 1) * size * scale + scale;
    }

    public long getSt() {
        if (st != null) return st;
        if (bar == null) throw new TodoRuntimeException();

        long scale = bar.getScale();
        long et = getEt();
        return this.st = et - size * scale;
    }

    public boolean next() {
        page++;
        long scale = bar.getScale();
        long now = SystemContext.currentTimeMillis();
        this.et = now - (page - 1) * size * scale + scale;
        this.st = this.et - size * scale;
        return true;
    }

}
