package ai.zzt.okx.emulator.repository;

import ai.zzt.okx.emulator.entity.ProfitRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitRecordRepository extends MongoRepository<ProfitRecordEntity, String> {
}
