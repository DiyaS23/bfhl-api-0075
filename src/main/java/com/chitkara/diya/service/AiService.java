package com.chitkara.diya.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create(
            "https://generativelanguage.googleapis.com"
    );

    private final ObjectMapper mapper = new ObjectMapper();

    public String ask(String question) {

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", question)
                                )
                        )
                )
        );



        String response = webClient.post()
                .uri("/v1beta/models/gemini-pro:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null) {
            throw new RuntimeException("AI response is null");
        }

        try {
            JsonNode root = mapper.readTree(response);

            String answer = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return answer.split("\\s+")[0];

        } catch (Exception e) {
            throw new RuntimeException("AI parsing failed");
        }
    }

}
