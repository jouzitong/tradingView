package ai.zzt.okx.emulator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;

@Data
@Document("trade_order_request")
@NoArgsConstructor
public class AlgoOrderEntity extends AlgoOrder {

    @Id
    private String id;

    @Deprecated // 这个属性应该可用现有的字段代替
    @Field("task_id")
    private String taskId;

    public static AlgoOrderEntity toEntity(String taskId, AlgoOrder algoOrder) {
        AlgoOrderEntity entity = new AlgoOrderEntity();

        entity.setAlgoClOrdId(algoOrder.getAlgoClOrdId());
        entity.setSz(algoOrder.getSz());
        entity.setActualSz(algoOrder.getActualSz());
        entity.setActualPx(algoOrder.getActualPx());
        entity.setActualSide(algoOrder.getActualSide());
        entity.setAmendPxOnTriggerType(algoOrder.getAmendPxOnTriggerType());
        entity.setAmendResult(algoOrder.getAmendResult());
        entity.setCcy(algoOrder.getCcy());
        entity.setClOrdId(algoOrder.getClOrdId());
        entity.setCTime(algoOrder.getCTime());
        entity.setFailCode(algoOrder.getFailCode());
        entity.setInstId(algoOrder.getInstId());
        entity.setInstType(algoOrder.getInstType());
        entity.setLast(algoOrder.getLast());
        entity.setLever(algoOrder.getLever());
        entity.setNotionalUsd(algoOrder.getNotionalUsd());
        entity.setOrdId(algoOrder.getOrdId());
        entity.setTaskId(taskId);
        entity.setOrdIdList(algoOrder.getOrdIdList());
        entity.setOrdPx(algoOrder.getOrdPx());
        entity.setOrdType(algoOrder.getOrdType());
        entity.setPosSide(algoOrder.getPosSide());
        entity.setReduceOnly(algoOrder.getReduceOnly());
        entity.setReqId(algoOrder.getReqId());
        entity.setSide(algoOrder.getSide());
        entity.setSlOrdPx(algoOrder.getSlOrdPx());
        entity.setSlTriggerPx(algoOrder.getSlTriggerPx());
        entity.setSlTriggerPxType(algoOrder.getSlTriggerPxType());
        entity.setState(algoOrder.getState());
        entity.setTag(algoOrder.getTag());
        entity.setTdMode(algoOrder.getTdMode());
        entity.setTgtCcy(algoOrder.getTgtCcy());
        entity.setTpOrdPx(algoOrder.getTpOrdPx());
        entity.setTpTriggerPx(algoOrder.getTpTriggerPx());
        entity.setTpTriggerPxType(algoOrder.getTpTriggerPxType());
        entity.setTriggerPx(algoOrder.getTriggerPx());
        entity.setTriggerPxType(algoOrder.getTriggerPxType());
        entity.setTriggerTime(algoOrder.getTriggerTime());
        entity.setUTime(algoOrder.getUTime());

        return entity;
    }

    public AlgoOrder toDTO() {
        return AlgoOrder.builder()
                .algoClOrdId(algoClOrdId)
                .algoId(algoId)
                .sz(sz)
                .actualSz(actualSz)
                .actualPx(actualPx)
                .actualSide(actualSide)
                .amendPxOnTriggerType(amendPxOnTriggerType)
                .amendResult(amendResult)
                .ccy(ccy)
                .clOrdId(clOrdId)
                .cTime(cTime)
                .failCode(failCode)
                .instId(instId)
                .instType(instType)
                .last(last)
                .lever(lever)
                .notionalUsd(notionalUsd)
                .ordId(ordId)
                .ordIdList(ordIdList)
                .ordPx(ordPx)
                .ordType(ordType)
                .posSide(posSide)
                .reduceOnly(reduceOnly)
                .reqId(reqId)
                .side(side)
                .slOrdPx(slOrdPx)
                .slTriggerPx(slTriggerPx)
                .slTriggerPxType(slTriggerPxType)
                .state(state)
                .tag(tag)
                .tdMode(tdMode)
                .tgtCcy(tgtCcy)
                .tpOrdPx(tpOrdPx)
                .tpTriggerPx(tpTriggerPx)
                .tpTriggerPxType(tpTriggerPxType)
                .triggerPx(triggerPx)
                .triggerPxType(triggerPxType)
                .triggerTime(triggerTime)
                .uTime(uTime)
                .build();
    }
}
