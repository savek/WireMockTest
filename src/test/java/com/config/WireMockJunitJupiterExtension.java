package com.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;


import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.function.Try.call;
import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.util.ReflectionUtils.findFields;
import static org.junit.platform.commons.util.ReflectionUtils.tryToReadFieldValue;

public class WireMockJunitJupiterExtension implements BeforeAllCallback, AfterAllCallback, ExecutionCondition {
    private static final ExtensionContext.Namespace NAMESPACE = Namespace.create(WireMockJunitJupiterExtension.class);
    private static final String WIREMOCK = "wiremock";

    @Override
    public void afterAll(ExtensionContext context) {
        final var server = context.getStore(NAMESPACE).get("wiremock", WireMockServer.class);
        server.stop();
        server.resetAll();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        final var server = new WireMockServer(getServerPort(context));

        findFields(context.getRequiredTestClass(), field -> field.isAnnotationPresent(WireMockMapping.class), TOP_DOWN)
                .stream()
                .map(field -> tryToReadFieldValue(field)
                        .andThen(obj -> call(() -> (MappingBuilder) obj))
                        .getOrThrow(RuntimeException::new)
                ).forEach(server::stubFor);

        context.getStore(NAMESPACE).put(WIREMOCK, server);
        server.start();
    }

    private int getServerPort(ExtensionContext context) {
        return context.getRequiredTestClass().getAnnotation(WireMockReg.class).port();
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        final var shouldRun = context.getRequiredTestClass().isAnnotationPresent(WireMockReg.class);
        return shouldRun ? enabled("Started WireMock server") : disabled("Skipped starting WireMock server");
    }
}