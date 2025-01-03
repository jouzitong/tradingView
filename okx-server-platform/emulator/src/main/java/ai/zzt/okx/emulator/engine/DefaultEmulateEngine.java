package ai.zzt.okx.emulator.engine;

import ai.zzt.okx.emulator.callBack.DefaultTaskCallBack;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * TODO 缺少取消任务后, 调用任务结束的逻辑, 可能会导致未知异常
 */
@Slf4j
public class DefaultEmulateEngine extends BaseEmulateEngine {

    public DefaultEmulateEngine(@NotNull DefaultTaskCallBack taskCallBack) {
        super(taskCallBack);
    }

}
