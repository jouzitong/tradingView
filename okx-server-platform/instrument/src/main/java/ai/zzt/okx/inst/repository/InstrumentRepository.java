package ai.zzt.okx.inst.repository;

import ai.zzt.okx.inst.entity.InstrumentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhouzhitong
 * @since 2024/11/14
 **/
@Repository
public interface InstrumentRepository extends MongoRepository<InstrumentEntity, String> {

    InstrumentEntity findByInstId(String instId);

    int deleteAllByInstId(String instId);

}
