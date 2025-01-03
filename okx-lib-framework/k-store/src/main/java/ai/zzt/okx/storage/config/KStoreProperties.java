package ai.zzt.okx.storage.config;

import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author wyz
 */
@Data
@Component
@ConfigurationProperties(prefix = "k-store")
public class KStoreProperties {

    // 存储路径
    private String storeRoot = "store/";

    /**
     * 支持计算的时间周期列表. 默认全部
     */
    private Set<Bar> supportBars = Set.of(Bar.s1, Bar.M_1,Bar.M_5, Bar.M_15, Bar.M_30,
            Bar.H_1, Bar.H_2, Bar.H_4,
            Bar.D_1, Bar.W_1, Bar.M1);

    /**
     * 是否支持磁盘存储
     */
    private boolean storageAble = false;

    /**
     * 支持存储的时间周期列表
     */
    private Set<Bar> storageBars = Set.of(Bar.s1);

    /**
     * k线计算周期
     */
    private int period = 10000;

    public Set<String> getHotInstIdList() {
        return null;
    }

}
