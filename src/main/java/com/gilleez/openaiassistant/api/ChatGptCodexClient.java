package com.gilleez.openaiassistant.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.gilleez.openaiassistant.core.ModelResponse;
import com.gilleez.openaiassistant.core.Response;
import com.gilleez.openaiassistant.settings.GilleezSettingsState;
import com.intellij.openapi.components.ServiceManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

public class ChatGptCodexClient {


    private final HttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl;
    private String apiKey;

    ChatGptCodexClient() {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                // Do I need to use company proxy if needed?
                //.proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
                .build();
        setUpProperties();
    }

    public void setUpProperties() {
        final GilleezSettingsState state = ServiceManager.getService(GilleezSettingsState.class);
        apiKey = state.getApiKey();
        baseUrl = state.baseUrl;
    }

    public Response fetchResultFromCodex(String model, String prompt) throws IOException, InterruptedException {

        ObjectNode req = objectMapper.createObjectNode();
        req.put("model", model);
        req.put("input", prompt);
        req.putObject("reasoning").put("effort", "high");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/responses"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(req)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return switch (response.statusCode()) {
            case 200 -> objectMapper.readValue(response.body(), Response.class);
            case 401 -> throw new RuntimeException("Api-key property is wrong");
            case 429 -> throw new RuntimeException("Limit is reached so please wait for a minute");
            case 500, 503 -> throw new RuntimeException("Open AI has some problems");
            default -> throw new RuntimeException("Unknown status exception from model listing");
        };
    }

    public ModelResponse fetchAvailableModelsForCodex() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/models"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return switch (response.statusCode()) {
            case 200 -> {
                ModelResponse modelResponse = objectMapper.readValue(response.body(), ModelResponse.class);
                modelResponse.setData(
                        modelResponse.getData().stream().filter(data ->
                                        data.getId().toLowerCase(Locale.ROOT).contains("codex".toLowerCase(Locale.ROOT)))
                                .toList());
                yield modelResponse;
            }
            case 401 -> throw new RuntimeException("Api-key property is wrong");
            case 429 -> throw new RuntimeException("Limit is reached so please wait for a minute");
            case 500, 503 -> throw new RuntimeException("Open AI has some problems");
            default -> throw new RuntimeException("Unknown status exception from model listing");
        };

    }

}
