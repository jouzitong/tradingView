package ai.zzt.okx.message.template;

import ai.zzt.okx.message.template.bo.InstReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2025/1/4
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportsTemplate implements MessageTemplate {

    /**
     * 产品信息报告
     */
    private List<InstReport> instReports;

    // 存储其他消息
    private List<String> otherMessages;

    // 存储注意事项
    private String notes;

    // 生成模板内容的方法
    public String generateTemplateContent() {
        StringBuilder content = new StringBuilder();
        content.append("# 股市信息通知\n\n");
        content.append("## 1. 今日涨幅信息\n\n");
        content.append("### 日涨幅:\n");

        for (InstReport instReport : instReports) {
            content.append("1. ").append(instReport.getId())
                    .append(", 今日涨幅: ").append(instReport.getDailyRise()).append("%")
                    .append(", 最小时涨幅: ").append(instReport.getLastHourlyRise()).append("%\n");
        }
        if (CollectionUtils.isNotEmpty(otherMessages)) {
            content.append("\n## 2. 其他消息\n");
            content.append("[在这里添加其他消息，如行业动态、政策调整等，可以用列表的形式展现]\n");
            for (String message : otherMessages) {
                content.append("- ").append(message).append("\n");
            }
        }

        if (StringUtils.hasText(notes)) {
            content.append("\n## 3. 注意事项\n");
            content.append(notes);
        }
        return content.toString();
    }


}
