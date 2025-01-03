package ai.zzt.okx.common.base.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor // FIXME 临时解决反序列化问题, 个人不建议直接使用无参构造, 子类同理
public abstract class BaseSettings implements Settings {

    /**
     * 产品/币种. 对应 instId/ccy 等
     * <p>
     * TODO 这个ID 好像没有用吧
     */
    @JsonIgnore
    @Deprecated
    private String id;

    public BaseSettings(String id) {
        this.id = id;
    }


}
