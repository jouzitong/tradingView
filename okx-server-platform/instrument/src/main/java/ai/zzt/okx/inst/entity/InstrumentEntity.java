package ai.zzt.okx.inst.entity;

import ai.zzt.okx.data.model.BaseInstrumentEntity;
import ai.zzt.okx.inst.bo.LinkBO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/11/12
 **/
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "instrument")
public class InstrumentEntity extends BaseInstrumentEntity {

    /**
     * 顺序
     */
    @Field("order")
    private Integer order = 100;

    /**
     * 标签
     */
    @Field("tags")
    private List<String> tags;

//    @JsonIgnore
//    private Map<InstrumentType, Instruments> instrumentsMap;

    /**
     * 简介
     */
    @Field("introduction")
    private String introduction;

    /**
     * 有关连接地址
     */
    @Field("official_links")
    private List<LinkBO> officialLinks;

    /**
     * 社交媒体地址
     */
    @Field("medias")
    private List<LinkBO> medias;

}
