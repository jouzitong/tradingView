package ai.zzt.okx.settings.permutations.param;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/9/9
 **/
@Data
public class ScrollParam {

    private Integer page = 0;

    private Integer size = 10_0000;

    private List<Integer> indexList = new ArrayList<>();

    public ScrollParam() {
        for (int i = 0; i < 10; i++) {
            indexList.add(0);
        }
    }

    public void updateIndex(Integer... is) {
        for (int i = 0; i < is.length; i++) {
            indexList.set(i, is[i]);
        }
    }

    public boolean hasNext() {
        // TODO: 2024/9/9 这里应该判断是否还有下一页的数据
        return true;
    }

}
