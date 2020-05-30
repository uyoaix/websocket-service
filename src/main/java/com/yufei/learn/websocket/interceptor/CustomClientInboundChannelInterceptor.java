package com.yufei.learn.websocket.interceptor;

import com.yufei.learn.websocket.client.AuthServiceClient;
import com.yufei.learn.websocket.domain.PassUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * custom ClientInboundChannel interceptor to process user jwtToken
 *
 * @author yufei.wang
 * @date 2020/05/23 17:12
 */
@Slf4j
@Component
public class CustomClientInboundChannelInterceptor implements ChannelInterceptor {

    private final AuthServiceClient authServiceClient;

    @Autowired
    public CustomClientInboundChannelInterceptor(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ||
                StompCommand.SEND.equals(accessor.getCommand())
        ) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            log.debug("webSocket token is {}", jwtToken);
            if (!StringUtils.isEmpty(jwtToken)) {

                PassUser passUser = authServiceClient.userInfo(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(passUser);

                accessor.setUser(passUser);
                // I try to remove above code : accessor.setUser(passUser);
                // Then SecurityContextChannelInterceptor line 123,
                // it will take user from message header with key "simpUser"
                // so if I remove , the user will change to anonymous, and request will be AccessDeniedException
            }
        }
        return message;
    }

}
