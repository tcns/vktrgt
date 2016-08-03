package ru.tcns.vktrgt.web.rest.util;

import org.springframework.http.HttpHeaders;
import ru.tcns.vktrgt.domain.external.vk.response.VKErrorResponse;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-vktrgtApp-alert", message);
        headers.add("X-vktrgtApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("vktrgtApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("vktrgtApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("vktrgtApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-vktrgtApp-error", "error." + errorKey);
        headers.add("X-vktrgtApp-params", entityName);
        return headers;
    }
    public static HttpHeaders createVKErrorHeader(VKErrorResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("errorCode", ""+response.getErrorCode());
        httpHeaders.add("errorMessage", response.getErrorMsg());
        httpHeaders.add("redirectUri", response.getRedirectUri());
        return httpHeaders;
    }
}
