package ai.zzt.okx.calculate.indicator.face.base;

import ai.zzt.okx.calculate.indicator.Indicator;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import lombok.Getter;
import lombok.Setter;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/7/17
 **/
@Getter
public abstract class BaseIndicatorFace<S extends BaseCalculateSettingsFace<?>>
        implements IndicatorFace<S> {

    @Setter
    protected S settingsFace;

    // 仅用于解决 json 序列化, 用户不应该直接获取
    protected final Map<Bar, ? extends Indicator<?>> indexMap;

    public BaseIndicatorFace(S settings) {
        this();
        this.settingsFace = settings;
    }

    public BaseIndicatorFace() {
        indexMap = new ConcurrentHashMap<>();
    }

    @Override
    public void init(Bar bar, MarkPrice mp) {
        check();
        Indicator<?> index = indexMap.computeIfAbsent(bar, this::createIndex);
        index.init(mp);
    }

    @Override
    public void add(MarkPrice mp) {
        check();
        Collection<Bar> bars = settingsFace.getBars();
        for (Bar bar : bars) {
            Indicator<?> index = indexMap.computeIfAbsent(bar, this::createIndex);
            index.add(mp.getMarkPx(), mp.getTs());
        }
    }


    @Override
    public <T extends Indicator<?>> T get(Bar bar) {
        check();
        return (T) indexMap.get(bar);
    }

    protected void check() {
        if (settingsFace == null) {
            throw new IllegalArgumentException("settings is null");
        }
    }

}
