package ai.zzt.okx.emulator.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.RandomUtils;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.propertoes.EmulatorProperties;
import ai.zzt.okx.emulator.serivce.EmulatorTaskManager;
import ai.zzt.okx.emulator.serivce.ITaskService;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟任务API
 *
 * @author zhouzhitong
 * @since 2024/10/1
 **/
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Resource
    private ITaskService taskService;

    @Resource
    private ISettingsService settingsService;


    @Resource
    private EmulatorTaskManager taskManager;

    @Resource
    private EmulatorProperties emulatorProperties;

    @Resource
    private OkxProperties okxProperties;

    /**
     * 应用模拟任务配置
     *
     * @param taskId 模拟任务ID
     * @return 是否成功
     */
    @PatchMapping("/{taskId}")
    public IR<Void> apply(@PathVariable("taskId") String taskId) {
        SettingsContext settingsContext = taskService.getSettingsByTaskId(taskId);
        if (settingsService == null) {
            return R.fail();
        }
        if (settingsService.add(settingsContext)) {
            return R.ok();
        } else {
            return R.fail();
        }
    }

    /**
     * 获取模拟任务配置
     *
     * @param taskId 模拟任务ID
     * @return 模拟任务配置
     */
    @GetMapping("/{taskId}")
    public IR<SettingsContext> get(@PathVariable("taskId") String taskId) {
        SettingsContext settingsContext = taskService.getSettingsByTaskId(taskId);
        return R.ok(settingsContext);
    }

    /**
     * 执行全部任务
     *
     * @param context 任务配置
     * @return 是否成功
     */
    @PostMapping("addAll")
    public R<Void> addAllTask(@RequestBody SettingsContext context,
                              @RequestParam(value = "storeEnable", required = false) Boolean storeEnable) {

        for (String instId : okxProperties.getHotInstIdList()) {
            String taskId = instId + RandomUtils.generateRandomString(16);
            CustomerTaskDTO task = new CustomerTaskDTO(instId, taskId);
            task.setBar(Bar.M_1);
            task.setSettingsContext(context.clone());
            task.getSettingsContext().setInstId(instId);
            taskService.addTask(task);
            if (storeEnable != null) {
                task.getTaskSettings().setStoreEnable(storeEnable);
            }
            taskManager.addTask(task);
        }
        return R.ok();
    }


    /**
     * 执行任务
     *
     * @param context 任务配置
     * @return 是否成功
     */
    @PostMapping
    public R<Void> addTask(@RequestBody SettingsContext context,
                           @RequestParam(value = "storeEnable", required = false) Boolean storeEnable,
                           @RequestParam(value = "st", required = false) String st,
                           @RequestParam(value = "et", required = false) String et) {

        // TODO 应该检验币种是否存在
        String instId = context.getInstId();
        Instruments instruments = InstrumentContext.get(instId);
        if (instruments == null) {
            return R.fail("币种不存在");
        }
        String taskId = instId + RandomUtils.generateRandomString(16);
        CustomerTaskDTO task = new CustomerTaskDTO(instId, taskId);
        if (st != null) {
            long startTime = DateTimeUtils.ofSecond(st);
            task.setStartTime(startTime);

        }
        task.setBar(Bar.M_1);
        task.setSettingsContext(context);
        taskService.addTask(task);
        if (storeEnable != null) {
            task.getTaskSettings().setStoreEnable(storeEnable);
        }
        taskManager.addTask(task);
        return R.ok();
    }

}
