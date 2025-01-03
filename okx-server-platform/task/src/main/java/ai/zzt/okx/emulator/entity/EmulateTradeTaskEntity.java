package ai.zzt.okx.emulator.entity;

import ai.zzt.okx.data.model.BaseEntity;
import ai.zzt.okx.emulator.dto.TaskResult;
import ai.zzt.okx.emulator.dto.TaskSettings;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.settings.context.SettingsContext;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "emulate_trade_task")
@ToString(callSuper = true)
public class EmulateTradeTaskEntity extends BaseEntity {

    /**
     * 任务组 ID
     * <p>
     * 描述: 通过自动化测试生成的任务, 都要带 groupId. 手动创建的任务则不应该带
     */
    @Field("group_id")
    @Nullable
    private String groupId;

    /**
     * 任务ID
     */
    @Field("task_id")
    private String taskId;

    /**
     * 产品ID
     */
    @Indexed
    @Field("inst_id")
    private String instId;

    /**
     * 产品类型
     */
    @Field("inst_type")
    private InstrumentType instType;

    /**
     * 优先级. 默认 1. 越大越优先.
     */
    @Field("priority")
    private int period = 1;

    /**
     * 开始时间(使用数据的). 暂时不用
     */
    @Field("开始时间")
    private Long startTime;

    /**
     * TODO 结束时间(使用数据的). 暂时不用
     */
    @Field("end_time")
    private Long endTime;

    /**
     * 时间周期
     */
    @Field("bar")
    private Bar bar;

    /**
     * 计算配置
     */
    @Field("settings_context")
    private SettingsContext settingsContext;

    /**
     * TODO 计算配置签名. 主要是用来区分不同的配置.
     */
    @Field("settings_context_signature")
    private String settingsContextSignature;

    /**
     * 任务配置
     */
    @Field("task_settings")
    private TaskSettings taskSettings;


    /**
     * 任务状态
     * <p>
     * TODO 这里枚举类型应该序列化成 code
     */
    @Field("status")
    private TaskStatus status = TaskStatus.NEW;

    /**
     * 最后结果集合
     */
    private TaskResult taskResult;

}
