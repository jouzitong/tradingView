package ai.zzt.okx.inst.entity;

import ai.zzt.okx.data.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "inst_tag")
public class TagEntity extends BaseEntity {

    /**
     * 标签
     */
    @Field("name")
    private String name;

    /**
     * 顺序
     */
    @Field("order")
    private Integer order = 100;

    /**
     * 描述信息
     */
    @Field("description")
    private String description;

}
