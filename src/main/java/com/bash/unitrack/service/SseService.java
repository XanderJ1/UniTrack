package com.bash.unitrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseService {
    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public SseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SseEmitter subscribe(String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for client: {}", clientId);
            clients.remove(clientId);
        });

        emitter.onTimeout(() -> {
            log.info("SSE connection timed out for client: {}", clientId);
            clients.remove(clientId);
        });

        emitter.onError((ex) -> {
            log.error("SSE connection error for client: {}", clientId, ex);
            clients.remove(clientId);
        });

        clients.put(clientId, emitter);
        log.info("New SSE client connected: {}", clientId);

        // Send initial connection confirmation
        try {
            emitter.send(SseEmitter.event()
                    .name("connection")
                    .data("Connected successfully"));
        } catch (IOException e) {
            log.error("Error sending initial message to client: {}", clientId, e);
            clients.remove(clientId);
        }

        return emitter;
    }

    public void sendSessionUpdate(String sessionId, String eventType, Object data) {
        Map<String, Object> message = Map.of(
                "type", "event_update",
                "eventId", sessionId,
                "eventType", eventType,
                "data", data,
                "timestamp", System.currentTimeMillis()
        );
        broadcastMessage("event_update", message);
    }

    private void broadcastMessage(String eventName, Object data) {
        clients.entrySet().removeIf(entry -> {
            try {
                entry.getValue().send(SseEmitter.event()
                        .name(eventName)
                        .data(objectMapper.writeValueAsString(data)));
                return false;
            } catch (IOException e) {
                log.error("Error sending message to client: {}", entry.getKey(), e);
                return true;
            }
        });
    }
}
