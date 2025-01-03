package ai.zzt.okx.emulator.serivce;

import ai.zzt.okx.data.req.BasePageReq;
import ai.zzt.okx.emulator.entity.EmulateTaskGroupEntity;
import ai.zzt.okx.settings.permutations.SettingsPermutations;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/1
 **/
public interface ITaskGroupService extends CommandLineRunner {

    /**
     * 添加任务组
     * @param instId 币种
     * @param settingsPermutations 任务组合配置
     * @return 是否添加成功
     */
    boolean addTaskGroup(String instId, SettingsPermutations settingsPermutations);

    boolean cancel(String groupId);

    List<EmulateTaskGroupEntity> findAll(BasePageReq req);

}
