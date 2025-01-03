package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.common.enums.SyncType;
import ai.zzt.okx.common.utils.SyncUtils;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持仓上下文. 开发时注意, 随时更新持仓信息, 可能会导致仓位信息为空的情况
 * <p>
 * TODO 应该都在 {@link AccountContext AccountContext} 账户上下文管理
 *
 * @author zhouzhitong
 * @since 2024/5/5
 **/
@Data
public class PositionContext {

    /**
     * 持仓
     * <p>
     * key: 产品id
     * value: 持仓
     */
    private Map<String, PositionFace> positionFaceMap = new ConcurrentHashMap<>();

    private volatile Long lastUpdateTime = 0L;

    /**
     * 全量同步持仓信息
     *
     * @param positions 全量持仓信息
     */
    public synchronized void addAll(List<Positions> positions) {
        // FIXME 确定是否需要清空
        positionFaceMap.clear();

        if (CollectionUtils.isEmpty(positions)) {
            return;
        }
        for (Positions position : positions) {
            add(position);
        }
        lastUpdateTime = positions.getLast().getUTime();
    }

    /**
     * 如果是全量同步, 禁止调用该接口, 进行更新数据
     *
     * @param position 持仓信息
     */
    public synchronized void add(Positions position) {
        String instId = position.getInstId();
        synchronized (SyncUtils.sync(instId, SyncType.POSITIONS)) {
            PositionFace positionWrapper = getFace(instId);
            positionWrapper.add(position);
        }
        lastUpdateTime = position.getUTime();
    }

    public synchronized void remove(String instId, PositionsSide posSide) {
        PositionFace positionFace = getFace(instId);
        positionFace.remove(posSide);
    }

    public synchronized void remove(String instId, String posId, PositionsSide posSide) {
        PositionFace positionFace = getFace(instId);
        positionFace.remove(posSide, posId);
    }

    public synchronized List<Positions> getPosition(String instId) {
        return getFace(instId).toList();
    }

    public synchronized List<PositionsWrapper> getPositionWrapper(String instId) {
        return getFace(instId).getWrappers();
    }

    public synchronized Positions getPosition(String instId, PositionsSide posSide) {
        return getFace(instId).get(posSide);
    }

    public synchronized PositionFace getFace(String instId) {
        return positionFaceMap.computeIfAbsent(instId, _ -> new PositionFace());
    }

    public synchronized List<Positions> getAllPosition(String instId) {
        PositionFace positionWrapper = getFace(instId);
        return positionWrapper.toList();
    }

    public synchronized void addPendingPosition(String instId) {
        PositionFace positionWrapper = getFace(instId);
        positionWrapper.pendingPositions = new Positions();
        positionWrapper.pendingPositions.setInstId(instId);
    }

    public boolean hasPendingPositions(String instId) {
        return positionFaceMap.get(instId).pendingPositions != null;
    }
}
