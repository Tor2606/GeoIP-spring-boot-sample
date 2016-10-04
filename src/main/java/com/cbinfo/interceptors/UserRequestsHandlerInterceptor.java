package com.cbinfo.interceptors;

import com.cbinfo.service.RequestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserRequestsHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Log LOGGER = LogFactory.getLog(UserRequestsHandlerInterceptor.class);

    @Autowired
    private RequestService requestService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOGGER.info(requestService.getLoggingMessage(request));
        requestService.saveHttpServletRequest(request);
    }
}