package com.cbinfo.service;

import com.cbinfo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by islabukhin on 27.09.16.
 */

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.DEFAULT)
public class UserSessionService {

    @Autowired
    private UserService userService;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserIfEmpty(){
        if(user == null) {
            org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            this.setUser(userService.findByEmail(principal.getUsername()));
        }
    }

}
