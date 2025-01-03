package ai.zzt.okx.emulator.repository;

import ai.zzt.okx.emulator.entity.EmulateTradeTaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmulateTradeTaskRepository extends MongoRepository<EmulateTradeTaskEntity, String> {

    EmulateTradeTaskEntity findByTaskId(String taskId);
//
//    void updateByTaskId(EmulateTradeTaskEntity entity);
//
//    @Query("{'status': ?0}")
//    List<EmulateTradeTaskEntity> findAllByStatus(TaskStatus status);

}
