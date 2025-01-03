package ai.zzt.okx.common.web.base.ws;

import ai.zzt.okx.common.web.base.IArgs;
import ai.zzt.okx.common.web.emus.ChannelType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhouzhitong
 * @since 2024/11/17
 **/
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "channel")
public abstract class BaseArgs implements IArgs {

    private ChannelType channel;

}
