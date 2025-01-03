package ai.zzt.okx.common.utils.print;

import ai.zzt.okx.common.utils.SlideList;

/**
 * @author zhouzhitong
 * @since 2024/8/11
 **/
public class SlideListStrUtils {

    public static String toStr(SlideList<? extends Str> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        StringBuilder sb = new StringBuilder();
        sb.append("size ").append(size).append(" ");
        sb.append("[");
        if (size == 0) {
            sb.append("]");
            return sb.toString();
        }

        for (int i = 0; i < size; i++) {
            sb.append(list.get(i).toStr()).append(", ");
        }

        return sb.toString();
    }

//    public static String toStrOfBigDecimal(SlideList<BigDecimal> list) {
//        StringBuilder sb = new StringBuilder();
//        int size = list.size();
//        sb.append("size ").append(size).append(" ");
//        sb.append("[");
//        if (size == 0) {
//            sb.append("]");
//            return sb.toString();
//        }
//        int i = 0;
//        BigDecimal start = list.get(i++);
//        sb.append(start);
//
//        for (; i < array.length; i++) {
//            BigDecimal next = (BigDecimal) get(i);
//            BigDecimal diff = next.subtract(start);
//            String sub = next.compareTo(BigDecimal.ZERO) > 0 ? " +" : " -";
//            sb.append(", ").append(diff)
//                    .append(sub)
//                    .append(diff.compareTo(BigDecimal.ZERO) > 0 ? "↑" : "↓");
//            start = next;
//        }
//
//        return sb.toString();
//        for (int i = 0; i < size; i++) {
//            sb.append(get(i));
//            if (i < size - 1) {
//                sb.append(", ");
//            }
//        }
//        sb.append("]");
//        return sb.toString();
//    }

}
