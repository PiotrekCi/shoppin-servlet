package org.shop.com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.json.JsonObject;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorMessages {
    public static final String INVALID_OR_MISSING_AUTHORIZATION_HEADER = "Invalid or missing Authorization header";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String FORBIDDEN = "Forbidden";

    public static String createErrorMessage(String errorMessage, HttpStatus httpStatus) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorMessage);
        response.put("status", httpStatus.toString());
        response.put("timestamp", LocalDateTime.now());
        return ConverterFactory.getStringFromObject(response);
    }
}
