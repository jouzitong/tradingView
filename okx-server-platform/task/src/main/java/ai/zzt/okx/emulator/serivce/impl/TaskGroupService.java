package ai.zzt.okx.emulator.serivce.impl;

import ai.zzt.okx.common.utils.RandomUtils;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.data.req.BasePageReq;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.entity.EmulateTaskGroupEntity;
import ai.zzt.okx.emulator.propertoes.EmulatorProperties;
import ai.zzt.okx.emulator.req.TaskGroupReq;
import ai.zzt.okx.emulator.serivce.ITaskGroupService;
import ai.zzt.okx.emulator.serivce.ITaskService;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.permutations.SettingsPermutations;
import ai.zzt.okx.settings.permutations.param.ScrollParam;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ai.zzt.okx.v5.enumeration.Bar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/10/1
 **/
@Service
@Slf4j
public class TaskGroupService implements ITaskGroupService {

    @Resource
    private ITaskService taskService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private EmulatorProperties properties;

//    @Resource
//    private LockService lockService;

    @Override
    public void run(String[] args) {
        log.info("initialize task group scheduled task.");
        TaskGroupReq req = new TaskGroupReq();
        req.setSize(2);
        req.setStatuses(Arrays.asList(TaskStatus.NEW, TaskStatus.RUNNING));
        // TODO 需要解决重复执行任务的问题
        ThreadUtils.submitScheduled(() -> {
            log.trace("find all task group. req: {}", req);
            if (!properties.isRunScheduledTaskGroup()) {
                log.trace("run scheduled task group is disabled.");
                return;
            }
            List<EmulateTaskGroupEntity> entities = findAll(req);
            CountDownLatch cb = new CountDownLatch(entities.size());
            entities.forEach(entity -> {
                ThreadUtils.runIO(() -> {
//                    if (lockService.tryLock(LockType.TASK_GROUP.getName(), entity.getGroupId(), 10 * 60)) {
                    runTask(entity);
                    cb.countDown();
//                    }
                    // 加锁失败, 说明其他服务正在执行任务, 不需要重复执行
                });
            });
            try {
                cb.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, 5, 5, TimeUnit.SECONDS);

    }

    @Override
    public boolean addTaskGroup(String instId, SettingsPermutations settingsPermutations) {
        log.info("create task. settingsPermutations: {}", JackJsonUtils.toStr(settingsPermutations));
        addEntity(instId, settingsPermutations);
        return true;
    }

    @Override
    public boolean cancel(String groupId) {
        log.info("cancel task group. groupId: {}", groupId);
        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("group_id").is(groupId)),
                Update.update("status", TaskStatus.CANCELLED),
                EmulateTaskGroupEntity.class);
        long modifiedCount = updateResult.getModifiedCount();
        return modifiedCount > 0;
    }

    @Override
    public List<EmulateTaskGroupEntity> findAll(BasePageReq req) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").in(TaskStatus.NEW, TaskStatus.RUNNING));
        query.with(Sort.by(Sort.Direction.DESC, "period"));
        query.limit(req.getSize());
        return mongoTemplate.find(query, EmulateTaskGroupEntity.class);
    }

    private boolean update(String groupId, ScrollParam param) {
        log.debug("update task group. groupId: {}, param: {}", groupId, JackJsonUtils.toStr(param));
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("group_id").is(groupId)),
                Update.update("param", param),
                EmulateTaskGroupEntity.class);
        return true;
    }

    /**
     * 判断当前任务组是否可执行
     *
     * @param groupId 任务组ID
     * @return true 可用; false 不可用
     */
    private boolean isAvailable(String groupId) {
        EmulateTaskGroupEntity entity = mongoTemplate.findOne(
                Query.query(Criteria.where("group_id").is(groupId)),
                EmulateTaskGroupEntity.class);
        return entity != null && entity.getStatus().getCode() <= TaskStatus.RUNNING.getCode();
    }

    public boolean running(String groupId) {
        log.debug("running task group. groupId: {}", groupId);
        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("group_id").is(groupId)),
                Update.update("status", TaskStatus.RUNNING),
                EmulateTaskGroupEntity.class);
        long modifiedCount = updateResult.getModifiedCount();
        return modifiedCount > 0;
    }

    private boolean finish(String groupId, ScrollParam param) {
        log.info("finish task group. groupId: {}, param: {}", groupId, JackJsonUtils.toStr(param));
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("group_id").is(groupId)),
                Update.update("param", param).set("status", TaskStatus.SUCCESS),
                EmulateTaskGroupEntity.class);
        return true;
    }

    private String addEntity(String instId, SettingsPermutations settingsPermutations) {
        log.info("[add entity. instId: {}", instId);
        EmulateTaskGroupEntity entity = new EmulateTaskGroupEntity();
        entity.setInstId(instId);
        entity.setGroupId(RandomUtils.generateRandomString(10));
        entity.setSettingsPermutations(settingsPermutations);
        entity.setParam(new ScrollParam());
        entity.setTotalCount(settingsPermutations.toSettingsCount(instId, new ScrollParam()));
        mongoTemplate.insert(entity);
        return entity.getGroupId();
    }

    private void runTask(EmulateTaskGroupEntity entity) {
        String groupId = entity.getGroupId();
        String instId = entity.getInstId();
        SettingsPermutations settingsPermutations = entity.getSettingsPermutations();
        long settingsCount = entity.getTotalCount();
        log.info("任务组: {}, 币种: {}, 预计需要生成的任务数量: {}", groupId, instId, settingsCount);
        // 滚动查询参数
        ScrollParam param = entity.getParam();
        if (param == null) {
            param = new ScrollParam();
        }
        long startTime = System.currentTimeMillis();
        running(groupId);
        long count = 0;
        while (true) {
            if (!isAvailable(groupId)) {
                log.warn("任务已取消: {}", groupId);
                return;
            }
            List<SettingsContext> settings = settingsPermutations.toSettings(instId, param);
            count += settings.size();
            if (settings.isEmpty()) {
                break;
            }
            if (Thread.interrupted()) {
                log.warn("任务初始化被中断");
                return;
            }
            List<CustomerTaskDTO> tasks = new ArrayList<>(10000);
            for (SettingsContext setting : settings) {
                String taskId = instId + RandomUtils.generateRandomString(16);
                CustomerTaskDTO task = new CustomerTaskDTO(instId, taskId);
                task.setGroupId(entity.getGroupId());
                task.setBar(Bar.M_1);
                task.setSettingsContext(setting);
                tasks.add(task);
                if (tasks.size() >= 10000) {
                    taskService.addAll(tasks);
                    update(groupId, param);
                    tasks.clear();
                }
            }
            if (!tasks.isEmpty()) {
                taskService.addAll(tasks);
                tasks.clear();
            }
        }
        finish(groupId, param);
        log.info("初始化任务耗时: {} ms, 总数: {}", System.currentTimeMillis() - startTime, count);
    }

}
