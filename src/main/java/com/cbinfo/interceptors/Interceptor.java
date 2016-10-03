package com.cbinfo.interceptors;

import com.cbinfo.service.RequestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Interceptor extends HandlerInterceptorAdapter {

    private static final Log LOGGER = LogFactory.getLog(Interceptor.class);

    @Autowired
    private RequestService requestService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info(requestService.getLoggingMessage(request));
        requestService.saveHttpServletRequest(request);
        return Boolean.TRUE;
    }
}