package ai.zzt.okx.dispatcher.engine.impl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ai.zzt.okx.v5.enumeration.PositionsSide;

/**
 * @author zhouzhitong
 * @since 2024/6/19
 **/
@Data
@AllArgsConstructor
public class PlaceOrderT {

    private String instId;

    private PositionsSide posSide;

    private long startTime;

}
