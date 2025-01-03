package ai.zzt.okx.emulator.repository;

import ai.zzt.okx.emulator.entity.AlgoOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgoOrderRepository extends MongoRepository<AlgoOrderEntity, String> {
}
