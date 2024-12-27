package com.example.spacecatsmarket.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Primary
    public WireMockServer mockOrderAuditService() {
        WireMockServer wireMockServer = new WireMockServer(options().port(8089));

        wireMockServer.stubFor(post(urlPathEqualTo("/order-audit/v1/orders"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"id\":\"test-id\",\"status\":\"SUCCESS\"}")));

        return wireMockServer;
    }
}
