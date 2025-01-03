import java.util.Calendar;

public class AsciiDocCalendar {

    public static void main(String[] args) {
        int year = Integer.valueOf(args[0]); // 设置年份
        int month = Integer.valueOf(args[1]); // 设置月份（注意：1月为0，2月为1，以此类推）

        String calendarAscii = generateCalendar(year, month);

        // 打印生成的日历
        System.out.println(calendarAscii);

        // 如果需要保存为文件，可以这样做：
        // try (java.io.FileWriter writer = new java.io.FileWriter("calendar_" + year + "_" + month + ".adoc")) {
        //     writer.write(calendarAscii);
        // } catch (java.io.IOException e) {
        //     e.printStackTrace();
        // }
    }

    public static String generateCalendar(int year, int month) {
        // 获取日历实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // 设置年份和月份（注意，月份是0-11）

        // 获取月份的名称
        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.CHINA);

        // 获取该月的最大天数
        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 获取该月的第一天是星期几
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 初始化AsciiDoc表格
        StringBuilder sb = new StringBuilder();
        sb.append("= ").append(monthName).append(" ").append(year).append("日历\n\n");
        sb.append("[options=\"header\"]\n|===\n");
        sb.append("| 日 | 一 | 二 | 三 | 四 | 五 | 六\n");

        // 生成空白日期前的空格
        int dayOfWeek = 1; // 周日
        for (int i = 1; i < firstDayOfWeek; i++) {
            sb.append("|   ");
            dayOfWeek++;
        }

        // 生成每一行的日期
        for (int day = 1; day <= maxDaysInMonth; day++) {
            sb.append("| ").append(String.format("%2d", day));

            // 每7天换一行
            if (dayOfWeek == 7) {
                sb.append("\n");
                dayOfWeek = 0;
            }

            dayOfWeek++;
        }

        // 如果最后一行没有满7个日期，填充空白
        while (dayOfWeek <= 7) {
            sb.append("|   ");
            dayOfWeek++;
        }

        sb.append("\n|===\n");

        return sb.toString();
    }
}
