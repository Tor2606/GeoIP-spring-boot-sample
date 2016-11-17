package com.cbinfo.interceptors;

import com.cbinfo.model.User;
import com.cbinfo.service.RequestService;
import com.cbinfo.service.UserService;
import com.cbinfo.service.UserSessionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class UserRequestsHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Log LOGGER = LogFactory.getLog(UserRequestsHandlerInterceptor.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionService userSessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        User user = userSessionService.getUser();
        updateUsersIP(request, user);
        LOGGER.info(requestService.getLoggingMessage(request));
        requestService.saveHttpServletRequest(request);
    }

    private void updateUsersIP(HttpServletRequest request, User user) {
        if (user != null) {
            if (isNotBlank(user.getUserIp()) || (!user.getUserIp().equals(request.getRemoteAddr()))) {
                user.setUserIp(request.getRemoteAddr());
                userService.saveUser(user);
            }
        }
    }
}