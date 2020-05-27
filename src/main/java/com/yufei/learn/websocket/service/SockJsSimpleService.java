package com.yufei.learn.websocket.service;

import com.yufei.learn.websocket.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yufei.wang
 * @date 2020/05/24 16:08
 */
@Slf4j
@Service
public class SockJsSimpleService {

    @Autowired
    private SimpMessagingTemplate simpleMessagingTemplate;

    public void sendToPublic(String msg){
        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        simpleMessagingTemplate.convertAndSend("/topic/greetings", JsonUtil.toJsonString(map));
    }
}
