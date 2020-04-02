package com.gj.interceptor;


import com.gj.common.EnumResponse;
import com.gj.common.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component//表示一个组件
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        String requestURI = request.getRequestURI();
//        log.info("请求url:{}",requestURI);
//        //刷新token时效性
//        String header = request.getHeader(EnumResponse.USER_REDIS_TOKEN);
//        String token = redis.get(header);
//
//        if(handler==null){
//            response.setContentType("text/html;charset=utf-8");
//            response.sendError(401,"请登录后操作");
//            return false;
//        }
//
//        if(handler!=null&&token==null){
//            response.setContentType("text/html;charset=utf-8");
//            response.sendError(401,"您已在其他地点登录");
//            return false;
//        }
//
//        redis.expire(header,EnumResponse.USER_REDIS_TOKEN_TIME);
        return true;
    }
}
