package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequest;
import com.cbinfo.repository.UserRequestsRepository;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class RequestService {

    private static final String LOGGING_MESSAGE_BEGINNING = "Incoming request(time, ip, url, users email): ";
    private static final String DELIMITER = ", ";


    @Autowired
    private UserRequestsRepository userRequestsRepository;

    @Autowired
    private UserSessionService userSessionService;

    public void saveHttpServletRequest(HttpServletRequest request) {
        UserRequest userRequest = new UserRequest();
        setRequestModelFields(userRequest, request);
        saveRequestModel(userRequest);
    }

    protected void setRequestModelFields(UserRequest userRequest, HttpServletRequest request) {
        userRequest.setIp(request.getRemoteAddr());
        userRequest.setTime(new Date());
        userRequest.setUrl(request.getRequestURI());
        userRequest.setUser(userSessionService.getUser());
    }

    public String getLoggingMessage(HttpServletRequest request) {
        return LOGGING_MESSAGE_BEGINNING + Joiner.on(DELIMITER).join(new Date().toString(), request.getRemoteAddr(),
                request.getRequestURI(), getUserEmail());
    }

    protected String getUserEmail() {
        User user = userSessionService.getUser();
        if(user == null) return "";
        return user.getEmail();
    }

    @Transactional
    protected void saveRequestModel(UserRequest userRequest){
        userRequestsRepository.save(userRequest);
    }
}
