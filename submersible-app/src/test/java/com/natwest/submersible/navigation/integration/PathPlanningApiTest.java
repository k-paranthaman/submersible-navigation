package com.natwest.submersible.navigation.integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Stream;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class PathPlanningApiTest {

    @Autowired
    private MockMvc mockMvc;

    private static Stream<Map<String, String>> provideTestPaths() {
        return Stream.of(

            Map.of(
                "requestPath", "path/request/invalid_req_grid.json",
                "responsePath", "path/response/invalid_res_grid.json"
            ),
            Map.of(
                "requestPath", "path/request/invalid_req_grid_max.json",
                "responsePath", "path/response/invalid_res_grid_max.json"
            ),
            Map.of(
                "requestPath", "path/request/invalid_req_initial_obstacle.json",
                "responsePath", "path/response/invalid_res_initial_obstacle.json"
            ),
            Map.of(
                "requestPath", "path/request/invalid_req_initial_out_boundry.json",
                "responsePath", "path/response/invalid_res_initial_out_boundry.json"
            ),
            Map.of(
                "requestPath", "path/request/invalid_req_target_obstacle.json",
                "responsePath", "path/response/invalid_res_target_obstacle.json"
            ),
            Map.of(
                "requestPath", "path/request/invalid_req_target_out_boundry.json",
                "responsePath", "path/response/invalid_res_target_out_boundry.json"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestPaths")
    void testPlanPath(Map<String, String> testPaths) throws Exception {
        // Arrange
        String requestJson = Files.readString(new ClassPathResource(testPaths.get("requestPath")).getFile().toPath());
        String expectedResponseJson = Files.readString(new ClassPathResource(testPaths.get("responsePath")).getFile().toPath());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/navigation/plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andReturn().getResponse();

        String responseBody = response.getContentAsString();

        // Assert
        assertThatJson(responseBody).isEqualTo(expectedResponseJson);
    }
}
