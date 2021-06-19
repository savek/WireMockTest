package com.config;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.github.tomakehurst.wiremock.client.WireMock;

/**
 * Регистрация сервера {@link WireMock}
 * */
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(WireMockJunitJupiterExtension.class)
public @interface WireMockReg {

    /** Порт сервера WireMock*/
    int port();

}