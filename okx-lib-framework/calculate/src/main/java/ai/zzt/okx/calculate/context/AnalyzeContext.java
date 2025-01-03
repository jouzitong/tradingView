package ai.zzt.okx.calculate.context;

import ai.zzt.okx.calculate.indicator.face.BollFace;
import ai.zzt.okx.calculate.indicator.face.KDJFace;
import ai.zzt.okx.calculate.indicator.face.KLineFace;
import ai.zzt.okx.calculate.indicator.face.MacdFace;
import ai.zzt.okx.calculate.indicator.face.RsiFace;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据分析上下文
 *
 * @author zhouzhitong
 * @since 2024/5/21
 **/
@Data
@NoArgsConstructor
public final class AnalyzeContext implements Serializable {

    /**
     * 交易产品ID
     */
    @JsonIgnore
    private String instId;

    @JsonIgnore
    private SettingsContext settingsContext;

    /**
     * 开始时间
     */
    private long start = SystemContext.currentTimeMillis();

    @JsonIgnore
    private List<BaseIndicatorFace> indicatorFaces;

    /**
     * FIXME 分析趋势: 上升、下降、震荡【整体趋势, 如果没有盈利也要平仓】
     * <p>
     * 如果为空, 则表示未分析趋势, 不具备下单条件
     */
    private TrendType analyzeTrend = TrendType.FLUCTUATE;

    /**
     * 局部趋势: 上升、下降、震荡【如果达到指定盈利也要平仓】
     */
    private TrendType localTrend = TrendType.FLUCTUATE;

    public AnalyzeContext(String instId, SettingsContext settingsContext) {
        this.instId = instId;
        this.settingsContext = settingsContext;
    }

    public void init(Bar bar, List<MarkPrice> mps) {
        getIndicatorFaces().forEach(indicatorFace -> indicatorFace.init(bar, mps));
    }

    public void add(MarkPrice mp) {
        getIndicatorFaces().forEach(indicatorFace -> indicatorFace.add(mp));
    }

    @JsonIgnore
    public synchronized List<BaseIndicatorFace> getIndicatorFaces() {
        if (CollectionUtils.isEmpty(indicatorFaces)) {
            indicatorFaces = new ArrayList<>();
            for (BaseCalculateSettingsFace<?> settingsFace : settingsContext.getCalculateSettingsFaceMap().values()) {
                IndicatorType calculateType = settingsFace.getIndicatorType();
                BaseIndicatorFace indicatorFace = switch (calculateType) {
                    case MACD -> new MacdFace(settingsContext.get(IndicatorType.MACD));
                    case RSI -> new RsiFace(settingsContext.get(IndicatorType.RSI));
                    case KDJ -> new KDJFace(settingsContext.get(IndicatorType.KDJ));
                    case K_LINE -> new KLineFace(settingsContext.get(IndicatorType.K_LINE));
                    case BOLL -> new BollFace(settingsContext.get(IndicatorType.BOLL));
                    default -> throw new TodoRuntimeException();
                };
                indicatorFaces.add(indicatorFace);
            }
        }
        return indicatorFaces;
    }

    public void clear() {
        this.analyzeTrend = TrendType.FLUCTUATE;
    }

}
