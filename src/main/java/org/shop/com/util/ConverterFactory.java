package org.shop.com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@UtilityClass
public class ConverterFactory {
    private static final ObjectMapper objectMapper = createObjectMapper();
    private static final ObjectMapper customizableObjectMapper = createObjectMapper();
    public static <T> T getObjectFromServletRequest(HttpServletRequest httpServletRequest, Class<T> objectClass) throws IOException {
        return objectMapper.readValue(getRequestBodyFromInputStream(httpServletRequest), objectClass);
    }

    public static <T> T getObjectFromServletRequestBody(HttpServletRequest httpServletRequest, Class<T> objectClass) throws IOException {
        return objectMapper.readValue(httpServletRequest.getAttribute("requestBody").toString(), objectClass);
    }

    public static <T> T getObjectFromString(String object, Class<T> objectClass) throws JsonProcessingException {
        return objectMapper.readValue(object, objectClass);
    }

    public static String getRequestBodyFromInputStream(HttpServletRequest httpServletRequest) throws IOException {
        InputStream requestBody = httpServletRequest.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        return jsonBuilder.toString();
    }

    public static <T> String getStringFromObject(T toConvert) throws JsonProcessingException {
        return objectMapper.writeValueAsString(toConvert);
    }

    public static <T> String getStringFromObjectIgnoringFields(T toConvert, String... fieldsToIgnore)
            throws JsonProcessingException {
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("customerFilter", SimpleBeanPropertyFilter.serializeAllExcept(fieldsToIgnore));
        customizableObjectMapper.setFilterProvider(filters);
        return customizableObjectMapper.writeValueAsString(toConvert);
    }

    public static String getPathVariable(HttpServletRequest httpServletRequest, int place) {
        return httpServletRequest.getPathInfo().split("/")[place];
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("customerFilter", SimpleBeanPropertyFilter.serializeAllExcept(""));
        objectMapper.setFilterProvider(filters);
        return objectMapper;
    }
}
