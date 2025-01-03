package ai.zzt.okx.emulator.manager;

import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.callBack.DefaultTaskCallBack;
import ai.zzt.okx.emulator.dto.TaskResult;
import ai.zzt.okx.emulator.engine.DefaultEmulateEngine;
import ai.zzt.okx.emulator.propertoes.EmulatorProperties;
import ai.zzt.okx.emulator.serivce.EmulatorTaskManager;
import ai.zzt.okx.emulator.serivce.ITaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * TODO 设计有点不合理, 任务结束时, 清理数据有点问题.
 *
 * @author zhouzhitong
 * @since 2024/8/5
 **/
@Service
@Slf4j
public class DefaultEmulatorTaskManager implements EmulatorTaskManager, CommandLineRunner {

    @Resource
    private ITaskService taskService;

    @Resource
    private EmulatorProperties properties;

    /**
     * 任务池, key: taskId, value: TaskStateInfo
     */
    protected final Map<String, TaskStateInfo> taskPool;

    public DefaultEmulatorTaskManager() {
        this.taskPool = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addTask(CustomerTaskDTO task) {
        String taskId = task.getTaskId();
        if (taskPool.containsKey(taskId)) {
            log.trace("任务已存在: {}", taskId);
            return false;
        }
        log.debug("添加待运行任务. task id: {}", task.getTaskId());
        // FIXME 这个数字应该可以配置
        if (taskPool.size() >= properties.getTask().getLimit()) {
            log.trace("任务池已满: {}", taskPool.size());
            return false;
        }
        // TODO 应该要有一个任务队列, 防止任务太多导致内存溢出
        DefaultEmulateEngine tradeTask = new DefaultEmulateEngine(new DefaultTaskCallBack(task));
        Future<TaskResult> future = ThreadUtils.callTask(tradeTask);
        taskPool.put(task.getTaskId(), new TaskStateInfo(tradeTask, future));
        return true;
    }

    @Override
    public boolean finish(String taskId) {
        TaskResult result = null;
        try {
            if (!taskPool.containsKey(taskId)) {
                log.warn("[完成任务] 任务不存在: {}", taskId);
                return false;
            }
            TaskStateInfo task = taskPool.get(taskId);
            result = task.task().result();
        } finally {
            taskService.finish(taskId, result);
            taskPool.remove(taskId);
        }
        return true;
    }

    @Override
    public boolean cancel(String taskId) {
        if (!taskPool.containsKey(taskId)) {
            log.warn("[取消任务] 任务不存在: {}", taskId);
            return false;
        }
        TaskStateInfo stateInfo = taskPool.get(taskId);
        try {
            stateInfo.future().cancel(true);
            taskPool.remove(taskId);
            return true;
        } catch (SecurityException e) {
            log.error("安全异常. ", e);
        } finally {
            taskService.cancelTask(taskId);
        }
        return false;
    }

    @Override
    public boolean running(String taskId) {
        taskService.running(taskId);
        return true;
    }

    @Override
    public boolean fail(String taskId, Throwable e) {
        log.error("[任务失败] taskId: {}, e: {}", taskId, e.getMessage());
        taskService.cancelTask(taskId);
        return false;
    }

    @Override
    public void run(String... args) {
        ThreadUtils.submitScheduled(() -> {
            if (!properties.getTask().isRunScheduledTask()) {
                log.trace("[定时任务] run scheduled task is disabled.");
                return;
            }
            if (!taskPool.isEmpty()) {
                return;
            }
            log.trace("[定时任务] 从数据库中获取未完成的任务，并提交到模拟引擎中。");
            List<CustomerTaskDTO> allNotFinishTask = taskService.getAllNotFinishTask(properties.getTask().getLimit());
            log.trace("获取到 {} 个未完成的任务。", allNotFinishTask.size());
            for (CustomerTaskDTO task : allNotFinishTask) {
                addTask(task);
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    private record TaskStateInfo(DefaultEmulateEngine task, Future<TaskResult> future) {

    }

}
