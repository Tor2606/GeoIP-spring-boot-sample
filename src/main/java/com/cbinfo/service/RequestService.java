package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequests;
import com.cbinfo.repository.UserRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class RequestService {

    private static final String LOGGING_MESSAGE_BEGINNING = "Incoming request(time, ip, url, users email): ";

    @Autowired
    private UserRequestsRepository userRequestsRepository;

    @Autowired
    private UserSessionService userSessionService;

    public void saveHttpServletRequest(HttpServletRequest request) {
        UserRequests userRequests = new UserRequests();
        setRequestModelFields(userRequests, request);
        saveRequestModel(userRequests);
    }

    private void setRequestModelFields(UserRequests userRequests, HttpServletRequest request) {
        userRequests.setIp(request.getRemoteAddr());
        userRequests.setTime(new Date());
        userRequests.setUrl(request.getRequestURI());
        userRequests.setUser(userSessionService.getUser());
    }

    public String getLoggingMessage(HttpServletRequest request) {
        StringBuilder messageBuilder = new StringBuilder(LOGGING_MESSAGE_BEGINNING);
        messageBuilder.append((new Date().toString()));
        messageBuilder.append(",");
        messageBuilder.append(request.getRemoteAddr());
        messageBuilder.append(",");
        messageBuilder.append(request.getRequestURI());
        messageBuilder.append(",");
        messageBuilder.append(getUserEmail());
        messageBuilder.append(",");
        return messageBuilder.toString();
    }

    private String getUserEmail() {
        User user = userSessionService.getUser();
        if(user == null) return "";
        return user.getEmail();
    }

    @Transactional
    protected void saveRequestModel(UserRequests userRequests){
        userRequestsRepository.save(userRequests);
    }
}
