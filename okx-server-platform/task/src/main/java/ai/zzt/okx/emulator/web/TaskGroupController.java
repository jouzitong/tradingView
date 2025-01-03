package ai.zzt.okx.emulator.web;

import ai.zzt.okx.common.base.IR;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.emulator.serivce.ITaskGroupService;
import ai.zzt.okx.settings.permutations.SettingsPermutations;
import ai.zzt.okx.settings.permutations.calculate.BollSettingsPermutations;
import ai.zzt.okx.settings.permutations.calculate.MacdSettingsPermutations;
import ai.zzt.okx.settings.permutations.calculate.RsiSettingsPermutations;
import ai.zzt.okx.settings.permutations.order.PlaceOrderSettingsPermutations;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务组 API
 *
 * @author zhouzhitong
 * @since 2024/9/23
 **/
@RestController
@RequestMapping("/api/v1/tasksGroups")
public class TaskGroupController {

    @Resource
    private ITaskGroupService taskGroupService;

    /**
     * 创建任务组
     *
     * @param instId               币种
     * @param settingsPermutations 配置组合
     * @return 是否成功
     */
    @PostMapping("/{instId}")
    public IR<Boolean> add(@PathVariable("instId") String instId, @RequestBody SettingsPermutations settingsPermutations) {
        boolean task = taskGroupService.addTaskGroup(instId, settingsPermutations);
        return R.ok(task);
    }

    /**
     * 获取默认的配置
     *
     * @return 默认的配置
     */
    @GetMapping("/defaultSettings")
    public IR<SettingsPermutations> getDefaultSettings() {
        SettingsPermutations settingsPermutations = new SettingsPermutations();
        settingsPermutations.setPlaceOrderSettings(new PlaceOrderSettingsPermutations());
        settingsPermutations.setMacdSettings(new MacdSettingsPermutations());
        settingsPermutations.setRsiSettings(new RsiSettingsPermutations());
        settingsPermutations.setBollSettings(new BollSettingsPermutations());
        return R.ok(settingsPermutations);
    }

    /**
     * 获取配置组合总数
     *
     * @param settingsPermutations 配置组合
     * @return 总数
     */
    @PatchMapping("/totalCount")
    public IR<Long> count(@RequestBody SettingsPermutations settingsPermutations) {
        return R.ok(settingsPermutations.size());
    }

    /**
     * 取消任务
     *
     * @param groupId 任务组ID
     * @return 是否成功
     */
    @DeleteMapping("/cancel/{groupId}")
    public IR<Boolean> cancel(@PathVariable("groupId") String groupId) {
        boolean task = taskGroupService.cancel(groupId);
        return R.ok(task);
    }

}
