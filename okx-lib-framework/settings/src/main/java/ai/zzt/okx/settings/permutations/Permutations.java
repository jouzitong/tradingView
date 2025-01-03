package ai.zzt.okx.settings.permutations;

import ai.zzt.okx.settings.permutations.param.ScrollParam;

import java.io.Serializable;
import java.util.List;

/**
 * 排列组合
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
public interface Permutations<T> extends Serializable {

    /**
     * 排列组合总数
     *
     * @return 排列组合总数
     */
    long size();

    /**
     * 根据产品ID生成配置
     *
     * @param instId 产品ID
     * @return 配置
     */
    List<T> toSettings(String instId);

    default List<T> toSettings(String instId, ScrollParam param) {
        return null;
    }

}
