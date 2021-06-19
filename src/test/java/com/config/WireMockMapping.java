package com.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.github.tomakehurst.wiremock.client.MappingBuilder;

/**
 * Аннотация уровня переменной, ставится над описанием заглушки типа {@link MappingBuilder}
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface WireMockMapping {
}