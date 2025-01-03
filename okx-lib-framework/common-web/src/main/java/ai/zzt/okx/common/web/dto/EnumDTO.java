package ai.zzt.okx.common.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 全局枚举DTO
 *
 * @author zhouzhitong
 * @since 2024/11/27
 **/
@Data
public class EnumDTO {

    /**
     * 确定传的参数
     */
    private Object code;

    /**
     * 枚举展示名称
     */
    private String name;

    private Boolean enable;

    /**
     * 具体枚举对象
     */
    @JsonIgnore
    private Enum<?> val;

}
