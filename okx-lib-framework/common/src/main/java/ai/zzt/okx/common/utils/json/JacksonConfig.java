package ai.zzt.okx.common.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;

/**
 * json 配置
 *
 * @author zhouzhitong
 * @since 2024/7/29
 **/
//@Configuration
public class JacksonConfig implements InitializingBean {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() {
        SimpleModule simpleModule = new SimpleModule();
        //指定mongodb ObjectId 序列化方式
//        simpleModule.addSerializer(ObjectI.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
    }
}
