package com.yufei.learn.websocket.config;

import com.yufei.learn.websocket.interceptor.CustomClientInboundChannelInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author yufei.wang
 * @date 2020/05/24 15:51
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private  CustomClientInboundChannelInterceptor customClientInboundChannelInterceptor;


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customClientInboundChannelInterceptor);
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/sockJs").setAllowedOrigins("*").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app","/user");
    }

}
