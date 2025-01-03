package ai.zzt.okx.emulator.entity;

import ai.zzt.okx.data.model.BaseEntity;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.settings.permutations.SettingsPermutations;
import ai.zzt.okx.settings.permutations.param.ScrollParam;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 任务组实体类
 *
 * @author zhouzhitong
 * @since 2024/9/25
 **/
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "emulate_task_group")
@ToString(callSuper = true)
public class EmulateTaskGroupEntity extends BaseEntity {

    /**
     * 任务组 ID
     */
    @Field("group_id")
    private String groupId;

    @Field("instId")
    private String instId;

    /**
     * 配置组合
     */
    @Field("settings_permutations")
    private SettingsPermutations settingsPermutations;

    /**
     * 预计总共需要生成的任务数量
     */
    @Field("total_count")
    private Long totalCount;

    /**
     * 滚动查询参数
     */
    @Field("param")
    @Nullable
    private ScrollParam param;

    /**
     * 任务状态
     */
    private TaskStatus status = TaskStatus.NEW;

}
