package com.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class HttpResponseUtil {

    /**
     * Получение списка объектов типа {@link T} из ответа сервера {@link HttpResponse}
     * @param response Ответ, пришедший от сервера
     * @param clazz Класс возвращаемого объекта
     * @return Список объектов типа {@link T}
     * */
    public static  <T> List<T> convertResponseToObjectsList(HttpResponse response, Class<T> clazz) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();

        ObjectMapper mapper = new ObjectMapper();
        List<T> retVal = mapper.readValue(responseString, new TypeReference<List<T>>() { });

        return retVal;
    }

    /**
     * Получение объекта типа {@link T} из ответа сервера {@link HttpResponse}
     * @param response Ответ, пришедший от сервера
     * @param clazz Класс возвращаемого объекта
     * @return Объект типа {@link T}
     * */
    public static <T> T convertResponseToObject(HttpResponse response, Class<T> clazz) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();

        ObjectMapper mapper = new ObjectMapper();
        T retVal = (T) mapper.readValue(responseString, clazz);

        return retVal;
    }

    public static String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

}
