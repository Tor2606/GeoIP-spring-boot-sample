package com.cbinfo.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Component
public class Interceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*if(request.getRequestURI().toLowerCase().startsWith(TRACKING_CONTROLLER_URI)) {
            return Boolean.TRUE;
        }
        Optional<Cookie> visitorIdCookie = HttpResponseHelper.getCookie(request, HttpResponseHelper.VISITOR_ID_COOKIE_NAME);
        UUID visitorId;
        if(!visitorIdCookie.isPresent()) {
            Visitor visitor = visitorIdentificationService.createVisitor(UUID.randomUUID());
            visitorId = visitor.getVisitorId();
            Cookie cookie = HttpResponseHelper.createCookie(HttpResponseHelper.VISITOR_ID_COOKIE_NAME, visitorId, request);
            response.addCookie(cookie);
            HttpResponseHelper.setCORSHeaders(response);
        }*/
        return Boolean.TRUE;
    }
}