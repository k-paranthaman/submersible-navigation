package com.natwest.submersible.navigation.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class NavigationApiTest {

    @Autowired
    private MockMvc mockMvc;



    private static Stream<Map<String, String>> provideTestPaths() {
        return Stream.of(
            Map.of(
                "requestPath", "navigation/request/request_success.json",
                "responsePath", "navigation/response/response_success.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_boundary.json",
                "responsePath", "navigation/response/response_invalid_boundary.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_command.json",
                "responsePath", "navigation/response/response_invalid_command.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_grid.json",
                "responsePath", "navigation/response/response_invalid_grid.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_grid_max.json",
                "responsePath", "navigation/response/response_invalid_grid_max.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_position_boundary.json",
                "responsePath", "navigation/response/response_invalid_position_boundary.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_invalid_position_failure.json",
                "responsePath", "navigation/response/response_invalid_position_failure.json"
            ),
            Map.of(
                "requestPath", "navigation/request/request_obstacle_found.json",
                "responsePath", "navigation/response/response_obstacle_found.json"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestPaths")
    void testExecuteNavigation(Map<String, String> testPaths) throws Exception {
        // Arrange
        String requestJson = Files.readString(new ClassPathResource(testPaths.get("requestPath")).getFile().toPath());
        String expectedResponseJson = Files.readString(new ClassPathResource(testPaths.get("responsePath")).getFile().toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/navigation/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andReturn().getResponse();

        String responseBody = response.getContentAsString(); // Extract response body as a string

        System.out.println("Received response :"+responseBody);
        // Assert
        assertThatJson(responseBody).isEqualTo(expectedResponseJson);
    }
}
