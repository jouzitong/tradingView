package ai.zzt.okx.common.base;

import ai.zzt.okx.common.utils.InstUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouzhitong
 * @since 2024/12/16
 **/
@Data
public class BaseInst implements Serializable {

    protected String instId;

    public String getId() {
        if (instId == null) {
            return null;
        }
        return InstUtils.getBase(instId);
    }

}
