package ai.zzt.okx.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Setter
@Getter
@ToString
public class BaseDTO {

    /**
     * 主键ID
     */
    protected String id;

    // FIXME 缺少创建用户和更新用户字段

    protected LocalDateTime updateTime;

    protected LocalDateTime createTime;

    /**
     * 版本号
     */
    protected String version;


}
