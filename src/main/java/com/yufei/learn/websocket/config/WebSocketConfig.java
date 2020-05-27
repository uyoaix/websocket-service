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
        // 添加自定义拦截器
        registration.interceptors(customClientInboundChannelInterceptor);
    }

    /**
     * 注册 Stomp的端点
     * addEndpoint：添加STOMP协议的端点。这个HTTP URL是供WebSocket或SockJS客户端访问的地址
     * withSockJS：指定端点使用SockJS协议
     *
     * @param registry registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJs 连接端点
        registry.addEndpoint("/sockJs").setAllowedOrigins("*").withSockJS();

    }

    /**
     * 配置消息代理：默认使用内置简单消息代理，可以使用MQ替换作为外部消息代理（需支持Stomp协议）
     * 启动简单Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
     *
     * @param config config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Spring 内置简单消息代理配置
        // 设置topic为广播订阅消息前缀，queue为点对点消息前缀
        config.enableSimpleBroker("/topic", "/queue");
        // 设置定时发送心跳
        //.setHeartbeatValue(new long[] {10000, 20000})
        //.setTaskScheduler(this.messageBrokerTaskScheduler);
        // 设置客户端请求前缀
        config.setApplicationDestinationPrefixes("/app","/user");


        // 使用RocketMQ 做外部消息代理
//        config
//                .enableStompBrokerRelay("/exchange", "/topic", "/queue", "/amq/queue")
//                .setRelayHost("172.16.0.20:9876")
//                .setClientLogin("hry")
//                .setClientPasscode("hry")
//                .setSystemLogin("hry")
//                .setSystemPasscode("hry")
//                .setSystemHeartbeatSendInterval(5000)
//                .setSystemHeartbeatReceiveInterval(4000);


    }

}
