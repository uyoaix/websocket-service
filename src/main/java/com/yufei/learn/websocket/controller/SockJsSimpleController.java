package com.yufei.learn.websocket.controller;

import com.yufei.learn.websocket.client.AuthServiceClient;
import com.yufei.learn.websocket.domain.PassUser;
import com.yufei.learn.websocket.service.SockJsSimpleService;
import com.yufei.learn.websocket.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author yufei.wang
 * @date 2020/05/20 23:06
 */
@Slf4j
@RestController
public class SockJsSimpleController {

    @Autowired
    private SockJsSimpleService sockJsSimpleService;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/testDefault")
    public String testSendDefault() {
        log.info("WebSocket send default");
        return "default success";
    }

    /**
     * send topic message
     *
     * @param msg message
     * @return String
     */
    @MessageMapping("/greeting")
    @SendTo("/topic/greetings")
    public String sendTo(Map<String, String> msg) {
        String greeting = JsonUtil.toJsonString(msg);
        log.info("client send message：msg={}", msg);

        return greeting;
    }

    /**
     * send message to anonymous user
     *
     * @return String
     */
    @MessageMapping("/user/sayHello")
    @SendToUser(value = "/queue/sayHello", broadcast = false)
    public String sayHello() {
        return JsonUtil.toJsonString("hello");
    }

    /**
     * send message to authenticate user
     *
     * @return String
     */
//    @MessageMapping("/user/info")
//    @SendToUser(value = "/queue/userInfo", broadcast = false)
//    public String userInfo(@AuthenticationPrincipal PassUser passUser) {
//        log.info("get user info：passUser={}", JsonUtil.toJsonString(passUser));
//        return JsonUtil.toJsonString(passUser);
//    }

    /**
     * send message to authenticate user
     *
     * @param passUser
     */
    @MessageMapping("/user/info")
    public void userInfo2(Principal passUser) {
        log.info("get user info：passUser={}", JsonUtil.toJsonString(passUser));
        simpMessagingTemplate.convertAndSendToUser(passUser.getName(), "/queue/userInfo", JsonUtil.toJsonString(passUser));
    }

//    @MessageMapping("/user/info")
//    @SendToUser(value = "/queue/userInfo", broadcast = false)
//    public String userInfo(@Header("Authorization") String token) {
//        if(!StringUtils.isEmpty(token)){
//            PassUser passUser = authServiceClient.userInfo(token);
//            log.info("get user info：passUser={}", JsonUtil.toJsonString(passUser));
//            return JsonUtil.toJsonString(passUser);
//        } else {
//            return  "token is null";
//        }
//    }

    @GetMapping("/send/public")
    public void sendToPublic() {
        log.info("sever send a message");
        String msg = "send a message";
        sockJsSimpleService.sendToPublic(msg);
    }

    @SubscribeMapping("/user/testSubscribeMessage")
    public String subScribeMessage() {
        return "SubscribeMessage";
    }


}
