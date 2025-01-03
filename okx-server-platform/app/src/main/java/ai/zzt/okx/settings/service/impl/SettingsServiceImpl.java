package ai.zzt.okx.settings.service.impl;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.dto.SettingsListDTO;
import ai.zzt.okx.settings.model.SettingsContextEntity;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.settings.repository.SettingContextRepository;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Service
@Slf4j
@AllArgsConstructor
@Primary
public class SettingsServiceImpl implements ISettingsService {

    private final SettingContextRepository settingContextRepository;

    private final AppProperties appProperties;

    public List<SettingsListDTO> getList() {
        List<SettingsContextEntity> entities = settingContextRepository.findAllByVersion(appProperties.getVersion());
        return entities.stream().map(e -> {
            SettingsListDTO dto = new SettingsListDTO();
            dto.setInstId(e.getInstId());
            dto.setEnablePlace(e.getPlaceOrderSettings().isEnablePlaceOrder());
            return dto;
        }).toList();
    }

    public boolean updateEnablePlaceOrder(String instId, boolean enable) {
        String baseCcy = InstUtils.getBase(instId);
        SettingsContextEntity entity = settingContextRepository.findByInstIdAndVersion(baseCcy, appProperties.getVersion());
        PlaceOrderSettings placeOrderSettings = entity.getPlaceOrderSettings();
        if (placeOrderSettings.isEnablePlaceOrder() == enable) {
            return true;
        }
        placeOrderSettings.setEnablePlaceOrder(enable);
        settingContextRepository.save(entity);
        return true;
    }

    @Override
    public boolean add(SettingsContext context) {
        context.setInstId(InstUtils.getBase(context.getInstId()));
        log.info("add settingContext: {}", context);

        // TODO 临时这么写 删除旧的配置
        remove(context.getInstId(), appProperties.getVersion());

        SettingsContextEntity entity = convert(context);
        settingContextRepository.save(entity);
        return true;
    }

    @Override
    public SettingsContext getSettings(String instId, String version) {
        Instruments instruments = InstrumentContext.get(instId);
        if (instruments == null) {
            throw new TodoRuntimeException("未知产品: " + instId);
        }
        if (version == null) {
            version = appProperties.getVersion();
        }
        SettingsContext context;
        synchronized (this) {
            String baseCcy = InstUtils.getBase(instId);
            SettingsContextEntity entity = settingContextRepository.findByInstIdAndVersion(baseCcy, version);
            if (entity == null) {
                entity = settingContextRepository.findByInstIdAndVersion(instId, version);
            }
            if (entity != null) {
                return convert(entity);
            }
            context = buildDefaultSettings(baseCcy);
        }
        return context;
    }

    @Override
    public boolean remove(String instId, String version) {
        int row = settingContextRepository.removeByInstIdAndVersion(InstUtils.getBase(instId), version);
        row += settingContextRepository.removeByInstIdAndVersion(instId, version);
        log.info("remove old settingContext: {}, row: {}", instId, row);
        return row > 0;
    }

    public List<SettingsContext> list() {
        List<SettingsContextEntity> entities = settingContextRepository.findAllByVersion(appProperties.getVersion());
        List<SettingsContext> res = new ArrayList<>(entities.size());
        for (SettingsContextEntity entity : entities) {
            res.add(convert(entity));
        }
        return res;
    }

    private SettingsContext buildDefaultSettings(String instId) {
        SettingsContextEntity entity;
        //        entity = convert(settingsContext);
//        settingContextRepository.save(entity);
//        log.info("saved settingContext.");
        return new SettingsContext(instId, true);
    }

    private SettingsContext convert(SettingsContextEntity entity) {
        SettingsContext context = new SettingsContext(entity.getInstId(), false);
        BeanUtils.copyProperties(entity, context);
        return context;
    }

    private SettingsContextEntity convert(SettingsContext context) {
        SettingsContextEntity entity = new SettingsContextEntity();
        BeanUtils.copyProperties(context, entity);
        entity.setVersion(appProperties.getVersion());
        return entity;
    }

    private String getBase(String instId) {
        Instruments instruments = InstrumentContext.get(instId, InstrumentType.SPOT);
        return instruments.getInstId();
    }

}
