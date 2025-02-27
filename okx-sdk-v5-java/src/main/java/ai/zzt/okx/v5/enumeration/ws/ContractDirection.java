package ai.zzt.okx.v5.enumeration.ws;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ContractDirection {

    LONG("long"),
    SHORT("short"),
    NEUTRAL("neutral");

    private final String value;

    ContractDirection(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }


}
