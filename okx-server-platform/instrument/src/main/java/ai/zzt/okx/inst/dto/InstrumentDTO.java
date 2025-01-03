package ai.zzt.okx.inst.dto;

import ai.zzt.okx.data.dto.BaseInstrumentDTO;
import ai.zzt.okx.inst.bo.LinkBO;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/9
 **/
@Setter
@Getter
@ToString(callSuper = true)
public class InstrumentDTO extends BaseInstrumentDTO {

    /**
     * 顺序
     */
    private Integer order = 100;

    /**
     * 标签
     */
    private List<String> tags;

    private Instruments instruments;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 有关连接地址
     */
    private List<LinkBO> officialLinks;

    /**
     * 社交媒体地址
     */
    private List<LinkBO> medias;

}
