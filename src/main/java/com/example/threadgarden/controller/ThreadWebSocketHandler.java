package com.example.threadgarden.controller;

import com.example.threadgarden.model.ThreadInfoDTO;
import com.example.threadgarden.service.ThreadMonitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ThreadWebSocketHandler extends TextWebSocketHandler {

    private final ThreadMonitorService service;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public ThreadWebSocketHandler(ThreadMonitorService service) {
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 1000)
    public void sendUpdates() throws Exception {

        List<ThreadInfoDTO> threads = service.getThreadData();
        Set<Long> deadlocks = service.getDeadlockedThreads();

        Map<String, Object> payload = new HashMap<>();
        payload.put("threads", threads);
        payload.put("deadlocks", deadlocks);

        String json = objectMapper.writeValueAsString(payload);

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }
}