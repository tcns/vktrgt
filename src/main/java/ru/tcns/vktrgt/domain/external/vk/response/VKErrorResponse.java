package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;

/**
 * Created by TIMUR on 09.07.2016.
 */
@JsonEntity
public class VKErrorResponse {
    private Integer errorCode;
    private String errorMsg;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
