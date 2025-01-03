package ai.zzt.okx.web.entity;

import ai.zzt.okx.data.model.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Data
@Document("user")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Field("username")
    private String username;

    /**
     * 密码
     */
    @Field("password")
    private String password;

    /**
     * 姓名
     */
    @Field("name")
    private String name;

    /**
     * 邮箱
     */
    @Field("email")
    private String email;

}
