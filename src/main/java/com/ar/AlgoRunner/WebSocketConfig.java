package com.ar.AlgoRunner;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // "topic" is where the server sends messages to (Subscribers listen here)
        config.enableSimpleBroker("/topic");
        // "app" is the prefix for messages sent FROM the client (e.g., /app/move)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint the React Frontend connects to
        registry.addEndpoint("/game-socket")
                .setAllowedOriginPatterns("*") // Allow React localhost connection
                .withSockJS(); // Fallback options if WebSocket is unavailable
    }}
