package ai.zzt.okx.web.repository;

import ai.zzt.okx.web.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
