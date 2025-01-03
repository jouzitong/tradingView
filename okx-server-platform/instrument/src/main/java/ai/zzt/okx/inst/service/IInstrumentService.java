package ai.zzt.okx.inst.service;

import ai.zzt.okx.inst.dto.InstrumentDTO;

import java.util.List;

/**
 * 产品 service
 *
 * @author zhouzhitong
 * @since 2024/12/9
 **/
public interface IInstrumentService {

    InstrumentDTO get(String instId);

    boolean add(InstrumentDTO instrument);

    boolean update(InstrumentDTO instrument);

    int delete(String instId);

    List<InstrumentDTO> list();

}
