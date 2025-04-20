package com.bash.Unitrack.Utilities;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class RouteRequest {

    private final WebClient webClient;

    public RouteRequest(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<String >computeRoute(RequestClass request, String apiKey){
        return webClient
                .post()
                .uri("https://routes.googleapis.com/directions/v2:computeRoutes")
                .header("ContentType", "application/json")
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", "routes.duration,routes.distanceMeters")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    return Mono.error(new RuntimeException("The error is: " + ex.getMessage()));
                });
    }
}
