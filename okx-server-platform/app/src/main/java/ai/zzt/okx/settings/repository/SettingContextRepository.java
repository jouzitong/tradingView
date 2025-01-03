package ai.zzt.okx.settings.repository;

import ai.zzt.okx.settings.model.SettingsContextEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/8/1
 **/
@Repository
public interface SettingContextRepository extends MongoRepository<SettingsContextEntity, String> {

    SettingsContextEntity findByInstIdAndVersion(String instId, String version);

    List<SettingsContextEntity> findAllByVersion(String version);

    int removeByInstIdAndVersion(String instId, String version);

}
