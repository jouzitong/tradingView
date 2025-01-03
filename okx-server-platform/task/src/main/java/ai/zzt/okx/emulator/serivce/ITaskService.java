package ai.zzt.okx.emulator.serivce;

import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.dto.TaskResult;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.permutations.SettingsPermutations;

import java.util.List;

/**
 * 任务持久化 Service, 并管理任务生命周期
 *
 * @author zhouzhitong
 * @since 2024/5/27
 **/
public interface ITaskService {

    /**
     * 批量创建任务
     *
     * @param instId               币种
     * @param settingsPermutations 任务配置
     * @return 是否成功
     */
    boolean createTask(String instId, SettingsPermutations settingsPermutations);

    /**
     * 添加任务
     *
     * @param task 任务
     * @return 是否添加成功
     */
    boolean addTask(CustomerTaskDTO task);

    void addAll(List<CustomerTaskDTO> tasks);

    /**
     * 执行任务
     *
     * @param taskId 任务ID
     * @return 是否执行成功
     */
    boolean running(String taskId);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param result 最后结果
     * @return 是否完成成功
     */
    boolean finish(String taskId, TaskResult result);

    /**
     * 取消任务
     *
     * @param task 任务
     * @return 是否取消成功
     */
    default boolean cancelTask(CustomerTaskDTO task) {
        return cancelTask(task.getTaskId());
    }

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return 是否取消成功
     */
    boolean cancelTask(String taskId);

    List<CustomerTaskDTO> list();

    /**
     * 获取所有未完成的任务
     *
     * @param limit 限制数量
     * @return 未完成的任务列表
     */
    List<CustomerTaskDTO> getAllNotFinishTask(int limit);

    /**
     * 根据任务ID获取任务配置
     *
     * @param taskId 任务ID
     * @return 任务配置
     */
    SettingsContext getSettingsByTaskId(String taskId);

}
