package ai.zzt.okx.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Configuration
@EnableMongoRepositories("ai.zzt.okx")
@EnableMongoAuditing
public class DataConfig {

    public DataConfig() {
        System.out.println("DataConfig");
    }

}
