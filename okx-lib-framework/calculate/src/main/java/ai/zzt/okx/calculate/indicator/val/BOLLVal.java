package ai.zzt.okx.calculate.indicator.val;

import ai.zzt.okx.calculate.vo.BOLLVO;
import lombok.Data;

/**
 * @author zhouzhitong
 * @since 2024/10/17
 **/
@Data
public class BOLLVal implements IndicatorVal {

    protected BOLLVO val = new BOLLVO();

}
