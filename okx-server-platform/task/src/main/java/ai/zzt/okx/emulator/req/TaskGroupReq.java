package ai.zzt.okx.emulator.req;

import ai.zzt.okx.data.req.BasePageReq;
import ai.zzt.okx.emulator.type.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/2
 **/
@Getter
@Setter
@ToString(callSuper = true)
public class TaskGroupReq extends BasePageReq {

    /**
     * 需要查询的任务状态
     */
    private List<TaskStatus> statuses;

}
