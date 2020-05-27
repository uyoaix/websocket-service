package com.yufei.learn.websocket.service.impl;

import com.yufei.learn.websocket.client.AuthServiceClient;
import com.yufei.learn.websocket.domain.PassUser;
import com.yufei.learn.websocket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author yufei.wang
 * @date 2020/05/24 19:32
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final AuthServiceClient authServiceClient;

    @Autowired
    public UserServiceImpl(AuthServiceClient authServiceClient){
        this.authServiceClient = authServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            Long userId = Long.valueOf(userName);
            PassUser user = authServiceClient.userInfo(userId);
            if (user == null) {
                log.warn("未查到用户[" + userName + "]信息");
                throw new UsernameNotFoundException("user not exist");
            }
            return user;
        } catch (Exception e) {
            log.error("查询用户失败:{}", e);
        }
        throw new UsernameNotFoundException("user not exist");
    }

}
