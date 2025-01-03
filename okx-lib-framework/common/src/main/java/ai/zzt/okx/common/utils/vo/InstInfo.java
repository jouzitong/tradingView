package ai.zzt.okx.common.utils.vo;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 产品信息
 *
 * @author zhouzhitong
 * @since 2024/11/30
 **/
@Setter
@Getter
@NoArgsConstructor
public class InstInfo implements Serializable {

    /**
     * 产品ID
     */
    private String id;

    /**
     * 基础币种
     */
    private String baseCcy = "USDT";

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 产品类型
     */
    private InstrumentType type;

    public InstInfo(String instId) {
        this.instId = instId;
        this.id = InstUtils.getBase(instId);
        this.type = InstUtils.getType(instId);
    }

    public String getCcy() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InstInfo instInfo = (InstInfo) o;
        return Objects.equals(instId, instInfo.instId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instId);
    }
}
