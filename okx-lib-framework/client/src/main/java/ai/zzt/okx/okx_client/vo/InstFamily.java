package ai.zzt.okx.okx_client.vo;

import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币种家族
 *
 * @author zhouzhitong
 * @since 2024/7/18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstFamily {

    /**
     * 币种 id (ccy)
     */
    private String ccy;
    /**
     * 币种家族集合
     */
    private Map<InstrumentType, Instruments> spotFamilyMap;

    public InstFamily(String ccy) {
        this.ccy = ccy;
    }

    public synchronized void add(InstrumentType type, Instruments instruments) {
        if (spotFamilyMap == null) {
            spotFamilyMap = new HashMap<>();
        }
        spotFamilyMap.put(type, instruments);
    }

    public synchronized List<Instruments> getList() {
        if (spotFamilyMap == null) {
            return List.of();
        }
        return new ArrayList<>(spotFamilyMap.values());
    }

    public Instruments get(InstrumentType type) {
        return spotFamilyMap.get(type);
    }

}
