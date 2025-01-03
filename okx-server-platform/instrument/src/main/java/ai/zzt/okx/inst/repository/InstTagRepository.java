package ai.zzt.okx.inst.repository;

import ai.zzt.okx.inst.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhouzhitong
 * @since 2024/11/14
 **/
@Repository
public interface InstTagRepository extends MongoRepository<TagEntity, String> {
}
