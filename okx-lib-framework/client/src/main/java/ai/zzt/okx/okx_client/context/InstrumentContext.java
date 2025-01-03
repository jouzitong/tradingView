package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.okx_client.vo.InstFamily;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.State;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/8/12
 **/
@Slf4j
public class InstrumentContext {

    /**
     * key: ccy
     * <p>
     * value: InstFamily
     */
    @Getter
    private static Map<String, InstFamily> map = new HashMap<>();

    public static void add(List<Instruments> instrumentsList) {
        instrumentsList.forEach(InstrumentContext::add);
    }

    public static void add(Instruments instruments) {
        // FIXME 目前基础货币只做 USDT的
        if (!instruments.getInstId().contains("USDT")) {
            return;
        }
        String instId = instruments.getInstId();
        InstrumentType type = instruments.getInstType();

        String baseCcy = InstUtils.getBase(instId);
        InstFamily instFamily;
        if (map.containsKey(baseCcy)) {
            instFamily = map.get(baseCcy);
        } else {
            instFamily = new InstFamily(baseCcy);
        }
        instFamily.add(type, instruments);
        map.put(baseCcy, instFamily);
    }

    public static boolean isEnable(String id) {
        String ccy = InstUtils.getBase(id);
        InstrumentType type = InstrumentType.SWAP;
        InstFamily instFamily = map.get(ccy);
        Instruments is = instFamily.get(type);
        if (is.getInstId().equals(id) && is.getState() == null) {
            return true;
        }
        return false;

    }

    /**
     * 获取所有产品
     *
     * @param type 产品类型
     * @return 产品列表
     */
    public static List<String> getAllEnableInstrumentsOfId(InstrumentType type) {
        List<String> res = new ArrayList<>();
        for (InstFamily isf : map.values()) {
            Instruments is = isf.get(type);
            if (is == null) {
                continue;
            }
            if (is.getState() == null || is.getState() == State.LIVE) {
                if (type == InstrumentType.SPOT) {
                    res.add(is.getBaseCcy());
                } else if (type == InstrumentType.SWAP) {
                    res.add(is.getCtValCcy());
                }
            }
        }
        return res;
    }

    public static Instruments get(String instId, InstrumentType type) {
        InstFamily instFamily = map.get(InstUtils.getBase(instId));
        if (instFamily == null) {
            return null;
        }
        return instFamily.get(type);
    }

    public static boolean isSupport(String instId, InstrumentType type) {
        InstFamily instFamily = map.get(InstUtils.getBase(instId));
        if (instFamily == null) {
            return false;
        }
        Instruments instruments = instFamily.get(type);
        return instruments != null;
    }

    public static Instruments get(String instId) {
        InstFamily instFamily = map.get(InstUtils.getBase(instId));
        if (instFamily == null) {
            log.error("instId: {} not found", instId);
            throw new TodoRuntimeException();
        }
        return instFamily.get(InstUtils.getType(instId));
    }

}
