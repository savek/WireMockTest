package com.savek.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.savek.dto.TestObjectDto;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.util.HttpResponseUtil.convertResponseToObject;
import static com.util.HttpResponseUtil.convertResponseToString;

@DisplayName("Использование WireMock с явной инициализацией")
public class WireMockTest {

    @Test
    @DisplayName("Тест на получение строки")
    public void simpleTest() throws IOException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/savek")).willReturn(aResponse().withBody("Welcome to savek!")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/savek");
        HttpResponse httpResponse = httpClient.execute(request);

        String responseString = convertResponseToString(httpResponse);
        System.out.println(responseString);

        wireMockServer.stop();
    }

    @Test
    @DisplayName("Тест на получение получение объекта")
    public void restTest() throws IOException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        // Тело заглушки
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/savek")).willReturn(aResponse()
                .withBody("{\"val\": \"Welcome to savek!\"}")
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")));

        // Запрос к серверу
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://localhost:8080/savek")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        HttpResponse httpResponse = client.execute(request);
        TestObjectDto testObjectDto = convertResponseToObject(httpResponse, TestObjectDto.class);
        System.out.println(testObjectDto);

        wireMockServer.stop();
    }

}
