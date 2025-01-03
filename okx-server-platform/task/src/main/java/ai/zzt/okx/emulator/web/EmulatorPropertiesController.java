package ai.zzt.okx.emulator.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.emulator.propertoes.EmulatorProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟器配置 API
 *
 * @author zhouzhitong
 * @since 2024/10/20
 **/
@RestController
@RequestMapping("/api/v1/emulatorProperties")
@Slf4j
public class EmulatorPropertiesController {

    @Resource
    private EmulatorProperties emulatorProperties;

    /**
     * 获取模拟器配置
     *
     * @return 模拟器配置
     */
    @GetMapping
    public R<EmulatorProperties> get() {
        return R.ok(emulatorProperties);
    }

    /**
     * 模拟器配置全量更新
     *
     * @param emulatorProperties 模拟器配置
     * @return
     */
    @PostMapping
    public R<EmulatorProperties> update(@RequestBody EmulatorProperties emulatorProperties) {
        log.info("update EmulatorProperties: {}", emulatorProperties);
        this.emulatorProperties.setRunScheduledTaskGroup(emulatorProperties.isRunScheduledTaskGroup());
        this.emulatorProperties.setTask(emulatorProperties.getTask());
        return R.ok(this.emulatorProperties);
    }

}
