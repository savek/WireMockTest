package com.savek.wiremock;

import com.config.WireMockMapping;
import com.config.WireMockReg;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.savek.dto.TestObjectDto;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.util.HttpResponseUtil.convertResponseToObject;
import static com.util.HttpResponseUtil.convertResponseToObjectsList;

@WireMockReg(port = 8080)
@DisplayName("Использование WireMock с помощью расширений JUnit extensions")
public class WireMockWithRuleTest {

    @WireMockMapping
    static MappingBuilder mappingBuilderList = get(urlPathEqualTo("/savekList"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withBody("[{\"val\": \"Welcome to the World!\"}, {\"val\": \"Welcome to the World! 2!\"}]")
                    .withHeader("Content-Type", "application/json"));

    @WireMockMapping
    static MappingBuilder mappingBuilderSingleObject = get(urlPathEqualTo("/savekObj"))
            .willReturn(aResponse()
                    .withStatus(200)
                    .withBody("{\"val\": \"Welcome to the World!\"}")
                    .withHeader("Content-Type", "application/json"));

    @Test
    @DisplayName("Проверка получения списка объектов")
    public void restTestList() throws IOException {
        // Запрос к серверу
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://localhost:8080/savekList")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        // Ответ от сервера
        HttpResponse httpResponse = client.execute(request);
        List<TestObjectDto> testObjectDtoList = convertResponseToObjectsList(httpResponse, TestObjectDto.class);
//        assertEquals(2l, testObjectDtoList.size());
        System.out.println(testObjectDtoList);
    }

    @Test
    @DisplayName("Проверка получения одного объекта")
    public void restTestObject() throws IOException {
        // Запрос к серверу
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://localhost:8080/savekObj")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        // Ответ от сервера
        HttpResponse httpResponse = client.execute(request);
        TestObjectDto testObjectDto = convertResponseToObject(httpResponse, TestObjectDto.class);

        TestObjectDto checkedDto = TestObjectDto.builder().val("Welcome to the World!").build();
//        assertEquals(checkedDto, testObjectDto);
        System.out.println(testObjectDto);
    }

}
