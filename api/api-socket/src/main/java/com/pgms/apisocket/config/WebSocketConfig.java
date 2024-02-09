package com.pgms.apisocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.pgms.apisocket.handler.CustomWebSocketHandlerDecorator;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	// private final WebsocketSecurityInterceptor websocketSecurityInterceptor;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/room")
			// .addInterceptors(new CustomHandshakeInterceptor())
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/from");
		registry.setApplicationDestinationPrefixes("/to");
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		registry.addDecoratorFactory(this::customWebSocketHandlerDecorator);
	}

	@Bean
	public CustomWebSocketHandlerDecorator customWebSocketHandlerDecorator(WebSocketHandler webSocketHandler) {
		return new CustomWebSocketHandlerDecorator(webSocketHandler);
	}

	// @Override
	// public void configureClientInboundChannel(ChannelRegistration registration) {
	// 	registration.interceptors(websocketSecurityInterceptor);
	// }
}
