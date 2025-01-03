package ai.zzt.okx.okx_client.context.bean;

import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FIXME 目前平仓只能全部平仓
 *
 * @author zhouzhitong
 * @since 2024/7/24
 **/
@Slf4j
public class PositionFace {

    /**
     * 多仓
     */
    public PositionsWrapper longPositions = new PositionsWrapper();

    /**
     * 空仓
     */
    public PositionsWrapper shortPositions = new PositionsWrapper();

    /**
     * 即将下单的仓位(已经下单, 但是还没来得及同步到持仓中)【TODO 是否应该考虑一个集合】
     * <p>
     * FIXME 如果通过ws实现的仓位同步, 可以计算下时间差, 如果能控制在0.5秒内, 这个字段可以不要了
     */
    public Positions pendingPositions;

    public Map<PositionsSide, Integer> posSideMap = new ConcurrentHashMap<>();

    /**
     * 最近一次开仓时间
     */
    public long lastOpenTime = 0;

    public boolean isEmpty() {
        return longPositions.positions == null && shortPositions.positions == null && pendingPositions == null;
    }

    public Positions get(PositionsSide posSide) {
        PositionsWrapper wrapper = getWrapper(posSide);
        if (wrapper == null) {
            return null;
        }
        return wrapper.getPositions();
    }

    public PositionsWrapper getWrapper(PositionsSide posSide) {
        if (posSide == PositionsSide.LONG) {
            return longPositions;
        } else if (posSide == PositionsSide.SHORT) {
            return shortPositions;
        }
        return null;
    }

    public List<PositionsWrapper> getWrappers() {
        List<PositionsWrapper> wrappers = new ArrayList<>();
        if (longPositions.positions != null) {
            wrappers.add(longPositions);
        }
        if (shortPositions.positions!=null){
            wrappers.add(shortPositions);
        }
        return wrappers;
    }


    /**
     * 添加临时仓位. 【TODO 应该还有其他参数】
     *
     * @param instId 产品ID
     */
    public void setPendPositions(String instId) {
        Positions positions = new Positions();
        positions.setInstId(instId);
        pendingPositions = positions;
    }

    public int getOpenCount() {
        return posSideMap.getOrDefault(PositionsSide.LONG, 0) + posSideMap.getOrDefault(PositionsSide.SHORT, 0);
    }

    public synchronized void add(Positions positions) {
        PositionsSide posSide = positions.getPosSide();
        if (posSide == PositionsSide.LONG) {
            if (longPositions == null) {
                longPositions = new PositionsWrapper(positions);
            }
            longPositions.positions = positions;
        } else {
            if (shortPositions == null) {
                shortPositions = new PositionsWrapper(positions);
            }
            shortPositions.positions = positions;
        }
        this.posSideMap.put(posSide, this.posSideMap.getOrDefault(posSide, 0) + 1);
        this.lastOpenTime = positions.getCTime();
        this.pendingPositions = null;
    }

    public boolean remove(PositionsSide side) {
        boolean flag = removePendingPositions(side, null);
        posSideMap.remove(side);
        return switch (side) {
            case LONG -> {
                if (longPositions != null) {
                    longPositions.clear();
                    yield true;
                }
                yield flag;
            }
            case SHORT -> {
                if (shortPositions != null) {
                    shortPositions.clear();
                    yield true;
                }
                yield flag;
            }
            default -> flag;
        };
    }

    public boolean remove(PositionsSide side, String posId) {
        boolean flag = removePendingPositions(side, posId);
        return switch (side) {
            case LONG -> {
                if (longPositions != null && longPositions.positions.getPosId().equals(posId)) {
                    longPositions.clear();
                    yield true;
                }
                yield flag;
            }
            case SHORT -> {
                if (shortPositions != null && shortPositions.positions.getPosId().equals(posId)) {
                    shortPositions.clear();
                    yield true;
                }
                yield flag;
            }
            default -> flag;
        };
    }

    public void updateProfit(BigDecimal px, long ts) {
        if (longPositions.positions != null) {
            longPositions.update(px, ts);
        }
        if (shortPositions.positions != null) {
            shortPositions.update(px, ts);
        }
    }


    public List<Positions> toList() {
        List<Positions> res = new LinkedList<>();
        if (longPositions.positions != null) {
            res.add(longPositions.positions);
        }
        if (shortPositions.positions != null) {
            res.add(shortPositions.positions);
        }
//        if (pendingPositions != null) {
//            res.add(pendingPositions);
//        }
        return res;
    }

    @JsonIgnore
    private boolean removePendingPositions(PositionsSide side, String posId) {
        // TODO pendingPositions#posId 大概率没有值. 所以这里只能通过side来判断
        if (pendingPositions != null && pendingPositions.getPosSide() == side) {
            pendingPositions = null;
            return true;
        }
        return false;
    }

}
