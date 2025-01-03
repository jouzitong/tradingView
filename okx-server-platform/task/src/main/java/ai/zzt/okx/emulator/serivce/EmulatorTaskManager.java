package ai.zzt.okx.emulator.serivce;


import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;

/**
 * 模拟任务管理器
 *
 * @author zhouzhitong
 * @since 2024/8/5
 **/
public interface EmulatorTaskManager {

    /**
     * 新增任务
     *
     * @param taskDTO 任务DTO
     * @return true: 成功, false: 失败
     */
    boolean addTask(CustomerTaskDTO taskDTO);

    /**
     * 执行任务
     *
     * @param taskId 任务ID
     * @return true: 成功, false: 失败
     */
    boolean running(String taskId);

    /**
     * 执行任务
     *
     * @param taskId 任务ID
     * @return true: 成功, false: 失败
     */
    boolean finish(String taskId);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return true: 成功, false: 失败
     */
    boolean cancel(String taskId);

    /**
     * 任务失败
     *
     * @param taskId 任务ID
     * @param e      失败异常
     * @return true: 成功, false: 失败
     */
    boolean fail(String taskId, Throwable e);

}
