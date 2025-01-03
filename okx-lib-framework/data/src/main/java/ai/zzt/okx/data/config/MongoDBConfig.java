package ai.zzt.okx.data.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author zhouzhitong
 * @since 2024/7/29
 **/
//@Configuration
@Slf4j
public class MongoDBConfig implements InitializingBean {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() {
        log.info("Initializing MongoDBConfig. 添加 json 配置.");
        SimpleModule simpleModule = new SimpleModule();
        //指定mongodb ObjectId 序列化方式
        simpleModule.addSerializer(ObjectId.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
    }

}