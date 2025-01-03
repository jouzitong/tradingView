package ai.zzt.okx.v5.entity.rest;

import ai.zzt.okx.v5.entity.IOkxObject;

/**
 * for rest api
 */
public interface IOkxRestObject<T> extends IOkxObject {

    int getCode();

    String getMsg();

    T getData();

    default boolean isOk() {
        return getCode() == 0;
    }

}
