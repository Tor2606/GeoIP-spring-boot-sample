package com.cbinfo.service;

import com.cbinfo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionService {

    @Autowired
    private UserService userService;

    protected User user;

    public User getUser() {
        setUserIfNull();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected void setUserIfNull() {
        if (user == null) {
            org.springframework.security.core.userdetails.User principal = getPrincipalFromSecurityContextHolder();
            findAndSetUserFromPrincipal(principal);
        }
    }

    protected void findAndSetUserFromPrincipal(org.springframework.security.core.userdetails.User principal) {
        if (principal != null) setUser(userService.findByEmail(principal.getUsername()));
    }

    protected org.springframework.security.core.userdetails.User getPrincipalFromSecurityContextHolder() {
        org.springframework.security.core.userdetails.User principal;
        try {
            principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
        return principal;
    }
}
