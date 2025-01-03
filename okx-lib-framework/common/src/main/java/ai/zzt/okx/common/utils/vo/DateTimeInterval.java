package ai.zzt.okx.common.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 时间区间
 *
 * @author zhouzhitong
 * @since 2024-08-19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeInterval {

    /**
     * 开始时间. 时间戳 ms
     */
    private long start;

    /**
     * 开始时间. 时间戳 ms
     */
    private long end;

}
