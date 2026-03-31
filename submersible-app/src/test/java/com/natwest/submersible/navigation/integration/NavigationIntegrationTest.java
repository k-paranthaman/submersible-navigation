package com.natwest.submersible.navigation.integration;

import com.natwest.submersible.navigation.integration.config.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RestTemplateConfig.class})
class NavigationIntegrationTest {

    private RestTemplate restTemplate = new RestTemplate();

    private String requestJson;
    private String expectedResponseJson;

    @BeforeEach
    void setUp() throws Exception {
        requestJson = Files.readString(new ClassPathResource("request/navigation-request.json").getFile().toPath());
        expectedResponseJson = Files.readString(new ClassPathResource("response/navigation-response.json").getFile().toPath());
    }

    @Test
    @Disabled
    void testExecuteNavigation() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/navigation/execute", HttpMethod.POST, requestEntity, String.class);

        // Assert
        assertThatJson(response.getBody()).isEqualTo(expectedResponseJson);
    }
}
