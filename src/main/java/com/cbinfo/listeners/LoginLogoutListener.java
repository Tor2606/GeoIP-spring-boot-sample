package com.cbinfo.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginLogoutListener implements ApplicationListener<AbstractAuthenticationEvent>, LogoutHandler {

    private static final Log LOG = LogFactory.getLog(LoginLogoutListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (LOG.isInfoEnabled() && event instanceof AuthenticationSuccessEvent
                || event instanceof AuthenticationFailureBadCredentialsEvent) {
            final StringBuilder builder = new StringBuilder();
            builder.append("LOGIN ");
            builder.append(ClassUtils.getShortName(event.getClass()));

            String userEmail = event.getAuthentication().getName();

            if(null == userEmail || userEmail.isEmpty()){
                builder.append(": loginFailure");
            } else {
                builder.append(": user email - ");
                builder.append(userEmail);
            }

            if (event instanceof AbstractAuthenticationFailureEvent) {
                builder.append("; exception: ");
                builder.append(((AbstractAuthenticationFailureEvent) event)
                        .getException().getMessage());
            }
            LOG.info(builder.toString());
        }
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        org.springframework.security.core.userdetails.User principalUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        LOG.info("LOGOUT: user login - " + principalUser.getUsername());
    }
}
