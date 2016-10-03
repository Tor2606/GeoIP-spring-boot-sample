package com.cbinfo.service;

import com.cbinfo.model.RequestModel;
import com.cbinfo.model.User;
import com.cbinfo.repository.RequestModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by islabukhin on 03.10.16.
 */
@Service
public class RequestService {

    private static final String LOGGING_MESSAGE_BEGINNING = "Incoming request(time, ip, url, users email): ";

    @Autowired
    private RequestModelRepository requestModelRepository;

    @Autowired
    private UserSessionService userSessionService;

    public void saveHttpServletRequest(HttpServletRequest request) {
        RequestModel requestModel = new RequestModel();
        setRequestModelFields(requestModel, request);
        saveRequestModel(requestModel);
    }

    private void setRequestModelFields(RequestModel requestModel, HttpServletRequest request) {
        requestModel.setIp(request.getRemoteAddr());
        requestModel.setTime(new Date());
        requestModel.setUrl(request.getRequestURI());
        requestModel.setUser(userSessionService.getUser());
    }

    public String getLoggingMessage(HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder(LOGGING_MESSAGE_BEGINNING);
        messageBuilder.append((new Date().toString()));
        messageBuilder.append(request.getRemoteAddr());
        messageBuilder.append(request.getRequestURI());
        messageBuilder.append(getUserEmail());
        return messageBuilder.toString();
    }

    private String getUserEmail() {
        userSessionService.setUserIfEmpty();
        User user = userSessionService.getUser();
        if(user == null) return "";
        return user.getEmail();
    }

    @Transactional
    protected void saveRequestModel(RequestModel requestModel){
        requestModelRepository.save(requestModel);
    }
}
