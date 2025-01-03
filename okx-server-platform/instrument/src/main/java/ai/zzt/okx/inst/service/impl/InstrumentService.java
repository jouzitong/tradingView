package ai.zzt.okx.inst.service.impl;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.inst.dto.InstrumentDTO;
import ai.zzt.okx.inst.entity.InstrumentEntity;
import ai.zzt.okx.inst.repository.InstrumentRepository;
import ai.zzt.okx.inst.service.IInstrumentService;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Service
@Slf4j
public class InstrumentService implements IInstrumentService {

    @Resource
    private InstrumentRepository repository;

    @Override
    public InstrumentDTO get(String instId) {
        InstrumentEntity entity = repository.findByInstId(instId);
        return toDTO(entity);
    }

    @Override
    public boolean add(InstrumentDTO instrument) {
        log.info("add Instrument: {}", instrument);
        delete(instrument.getInstId());
        InstrumentEntity entity = toEntity(instrument);
        repository.save(entity);
        return true;
    }

    @Override
    public boolean update(InstrumentDTO instrument) {
        String id = instrument.getId();
        if (id == null) {
            throw new TodoRuntimeException();
        }
        InstrumentEntity entity = toEntity(instrument);
        repository.save(entity);
        return true;
    }

    @Override
    public int delete(String instId) {
        int row = repository.deleteAllByInstId(instId);
        log.info("delete Instrument:{},  row: {}", instId, row);
        return row;
    }

    @Override
    public List<InstrumentDTO> list() {
        List<InstrumentEntity> all = repository.findAll();
        return all.stream().map(this::toDTO).toList();
    }

    private InstrumentEntity toEntity(InstrumentDTO dto) {
        InstrumentEntity entity = new InstrumentEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    private InstrumentDTO toDTO(InstrumentEntity entity) {
        if (entity == null) {
            return null;
        }
        InstrumentDTO dto = new InstrumentDTO();
        BeanUtils.copyProperties(entity, dto);
        Instruments instruments = InstrumentContext.get(entity.getInstId());
        dto.setInstruments(instruments);
        return dto;
    }

}
