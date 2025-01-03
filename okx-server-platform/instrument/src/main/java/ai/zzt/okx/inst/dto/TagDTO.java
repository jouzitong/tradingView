package ai.zzt.okx.inst.dto;

import ai.zzt.okx.data.dto.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "inst_tag")
public class TagDTO extends BaseDTO {

    /**
     * 标签
     */
    private String name;

    /**
     * 顺序
     */
    private Integer order = 100;

    /**
     * 描述信息
     */
    private String description;

}
