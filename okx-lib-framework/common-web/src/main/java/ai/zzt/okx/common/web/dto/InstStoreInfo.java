package ai.zzt.okx.common.web.dto;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.utils.vo.TimeRange;
import ai.zzt.okx.v5.enumeration.Bar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@Data
public class InstStoreInfo {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * key: 时间粒度
     * <p>
     * value: 已经存储了的时间区间
     */
    private Map<Bar, TimeRange> barsDateMap;

    /**
     * 所有的文件
     */
    @JsonIgnore
    private Map<Bar, List<String>> fileBarMap;

    public String getCcy() {
        return InstUtils.getBase(instId);
    }

    /**
     * 获取已经存储了的时间区间
     *
     * @param bar 时间粒度
     * @return 已经存储了的时间区间
     */
    public TimeRange getInterval(Bar bar) {
        if (barsDateMap == null) {
            return null;
        }
        return barsDateMap.get(bar);
    }

}
