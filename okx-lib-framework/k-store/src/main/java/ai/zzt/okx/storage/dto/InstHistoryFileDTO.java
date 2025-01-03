package ai.zzt.okx.storage.dto;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 产品历史数据文件说明 DTO
 *
 * @author zhouzhitong
 * @since 2024/12/17
 **/
@Data
@NoArgsConstructor
public class InstHistoryFileDTO {

    private String instId;

    private Map<Bar, ?> map;

    public InstHistoryFileDTO(String instId) {
        this.instId = instId;
    }

    public String getId() {
        return InstUtils.getBase(instId);
    }

    public String getCcy() {
        return getId();
    }

}
