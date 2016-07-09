package ru.tcns.vktrgt.domain.external.vk.exception;

import ru.tcns.vktrgt.domain.external.vk.response.VKErrorResponse;

/**
 * Created by TIMUR on 09.07.2016.
 */
public class VKException extends Exception {
    private VKErrorResponse vkErrorResponse;

    public VKException(VKErrorResponse vkErrorResponse) {
        super(vkErrorResponse.getErrorMsg());
        this.setVkErrorResponse(vkErrorResponse);
    }
    public VKErrorResponse getVkErrorResponse() {
        return vkErrorResponse;
    }

    private void setVkErrorResponse(VKErrorResponse vkErrorResponse) {
        this.vkErrorResponse = vkErrorResponse;
    }
}
