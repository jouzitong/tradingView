package ai.zzt.okx.okx_client.utils;

import ai.zzt.okx.v5.entity.ws.pri.Positions;

/**
 * 数据可视化展示工具类
 *
 * @author zhouzhitong
 * @since 2024/5/17
 **/
public class DataVisualizeShowUtils {

    public static String show(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Positions positions) {
            return positions(positions);
        }

        return o.toString();
    }


    private static String positions(Positions position) {


        return null;
    }

}
