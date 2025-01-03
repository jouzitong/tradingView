package ai.zzt.okx.common.utils.vo;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间区间
 *
 * @author zhouzhitong
 * @since 2024/12/17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeRange {

    /**
     * 开始时间
     */
//    @JsonFormat(pattern = "yyyy-MM")
    private String start;

    /**
     * 结束时间
     */
//    @JsonFormat(pattern = "yyyy-MM")
    private String end;

    public List<String> getExtraMonths() {
        return getExtraMonths(null);
    }

    public List<String> getExtraMonths(@Nullable TimeRange other) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 将时间字符串转换为LocalDate对象
        LocalDate startA = LocalDate.parse(this.start + "-01");
        LocalDate endA = LocalDate.parse(this.end + "-01");

        // 结果集合
        List<String> extraMonths = new ArrayList<>();
        // 如果other为null，直接返回A的所有年月
        if (other == null) {
            LocalDate current = startA;
            while (!current.isAfter(endA)) {
                extraMonths.add(current.format(formatter));
                current = current.plusMonths(1);
            }
            return extraMonths;
        }
        extraMonths.add(other.getEnd());

        // 将other的时间字符串转换为LocalDate对象
        LocalDate startB = LocalDate.parse(other.start + "-01");
        LocalDate endB = LocalDate.parse(other.end + "-01");

        // 遍历A的时间范围
        LocalDate current = startA;
        while (!current.isAfter(endA)) {
            // 如果当前时间不在B的时间范围内，加入结果集合
            if (current.isBefore(startB) || current.isAfter(endB)) {
                extraMonths.add(current.format(formatter));
            }
            current = current.plusMonths(1); // 移动到下一个月
        }
        return extraMonths;
    }

    public static void main(String[] args) {
        // 对象A和B的时间范围
        TimeRange rangeA = new TimeRange("2023-06", "2024-01");
        TimeRange rangeB = new TimeRange("2023-01", "2023-10");

        // 获取A相对于B多出的年月
        List<String> extraMonths = rangeA.getExtraMonths(null);

        // 输出结果
        System.out.println("A相对于B多出的年月: " + extraMonths);
    }

}
