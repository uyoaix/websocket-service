package com.yufei.learn.websocket.client;

import com.yufei.learn.websocket.domain.PassUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author yufei.wang
 * @date 2020/05/25 00:17
 */
@FeignClient(name = "auth-service", url = "120.27.218.194:8081")
public interface AuthServiceClient {

    @GetMapping("/user/info/{userId}")
    PassUser userInfo(@PathVariable("userId") Long userId);

    @GetMapping("/user/token_verify")
    PassUser userInfo(@RequestHeader("Authorization") String token);

}
