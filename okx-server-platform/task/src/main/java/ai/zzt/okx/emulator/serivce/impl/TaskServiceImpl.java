package ai.zzt.okx.emulator.serivce.impl;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.RandomUtils;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.dto.TaskResult;
import ai.zzt.okx.emulator.entity.EmulateTradeTaskEntity;
import ai.zzt.okx.emulator.propertoes.EmulatorProperties;
import ai.zzt.okx.emulator.serivce.ITaskService;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.permutations.SettingsPermutations;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务持久化 Service 实现
 */
@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private EmulatorProperties properties;

    // TODO 应该将这个任务存储到数据中, 定时去拉取. 避免同一时间生成的任务太多了
    // @See EmulateTaskGroupEntity
    @Override
    public boolean createTask(String instId, SettingsPermutations settingsPermutations) {
        return false;
    }

    @Override
    public void addAll(List<CustomerTaskDTO> tasks) {
        log.info("[addAllTask] 新增任务: {}", tasks.size());
        List<EmulateTradeTaskEntity> taskEntities = tasks.stream().map(this::convert).toList();
        mongoTemplate.insertAll(taskEntities);
    }

    @Override
    public boolean addTask(CustomerTaskDTO task) {
        EmulateTradeTaskEntity taskEntity = convert(task);
        log.info("新增任务: {}", taskEntity.getTaskId());
        mongoTemplate.insert(taskEntity);
        return true;
    }

    @Override
    public boolean finish(String taskId, TaskResult result) {
        log.debug("任务结束: {}", taskId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("taskId").is(taskId)),
                Update.update("status", TaskStatus.SUCCESS).set("task_result", result),
                EmulateTradeTaskEntity.class);
        return true;
    }

    @Override
    public boolean running(String taskId) {
        log.debug("任务运行中: {}", taskId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("taskId").is(taskId)),
                Update.update("status", TaskStatus.RUNNING),
                EmulateTradeTaskEntity.class);
        return true;
    }

    @Override
    public boolean cancelTask(String taskId) {
        log.debug("任务取消: {}", taskId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("taskId").is(taskId)
                        .and("status").is(TaskStatus.RUNNING)),
                Update.update("status", TaskStatus.CANCELLED),
                EmulateTradeTaskEntity.class);
        return true;
    }

    @Override
    public List<CustomerTaskDTO> list() {
        throw new TodoRuntimeException();
    }

    @Override
    public List<CustomerTaskDTO> getAllNotFinishTask(int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").in(TaskStatus.NEW, TaskStatus.RUNNING));
        query.with(Sort.by(Sort.Direction.DESC, "period"));
        query.limit(limit);
        List<EmulateTradeTaskEntity> emulateTradeTaskEntities = mongoTemplate.find(query, EmulateTradeTaskEntity.class);
        return emulateTradeTaskEntities.stream().map(this::convert).toList();
    }

    @Override
    public SettingsContext getSettingsByTaskId(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("task_id").is(taskId));
        EmulateTradeTaskEntity entity = mongoTemplate.findOne(query, EmulateTradeTaskEntity.class);
        return entity == null ? null : entity.getSettingsContext();
    }

    private CustomerTaskDTO convert(EmulateTradeTaskEntity entity) {
        CustomerTaskDTO task = new CustomerTaskDTO(entity.getInstId(), entity.getTaskId());

        task.setBar(entity.getBar());
        task.setSettingsContext(entity.getSettingsContext());
        task.setTaskSettings(entity.getTaskSettings());
        task.setStartTime(entity.getStartTime());
        task.setEndTime(entity.getEndTime());

        return task;
    }

    private EmulateTradeTaskEntity convert(CustomerTaskDTO task) {
        EmulateTradeTaskEntity taskEntity = new EmulateTradeTaskEntity();
        taskEntity.setInstId(task.getInstId());
        taskEntity.setTaskId(task.getTaskId());
        taskEntity.setPeriod(RandomUtils.randomInt());
        taskEntity.setBar(task.getBar());
        taskEntity.setEndTime(task.getEndTime());
        taskEntity.setStartTime(task.getStartTime());
        taskEntity.setSettingsContext(task.getSettingsContext());
        taskEntity.setTaskSettings(task.getTaskSettings());
        return taskEntity;
    }
}
