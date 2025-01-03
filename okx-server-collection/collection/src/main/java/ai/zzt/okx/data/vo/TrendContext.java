package ai.zzt.okx.data.vo;

import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Component
@Slf4j
public class TrendContext {

    private final Set<String> upInstIds = new CopyOnWriteArraySet<>();

    private final Set<String> downInstIds = new CopyOnWriteArraySet<>();

    public void update(String instId, TrendType trend) {
        remove(instId);
        if (trend == TrendType.UP) {
            upInstIds.add(instId);
        } else if (trend == TrendType.DOWN) {
            downInstIds.add(instId);
        }
    }

    private void remove(String instId) {
        upInstIds.remove(instId);
        downInstIds.remove(instId);
    }

    public List<String> getList(TrendType type) {
        if (type == TrendType.UP) {
            return new ArrayList<>(upInstIds);
        } else if (type == TrendType.DOWN) {
            return new ArrayList<>(downInstIds);
        }
        throw new TodoRuntimeException();
    }

}
