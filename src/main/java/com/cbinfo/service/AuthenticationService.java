package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service
public class AuthenticationService {

    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    @Qualifier(value = "myAuthenticationManager")
    @Lazy
    protected AuthenticationManager authenticationManager;

    public void authenticate(UserForm user, HttpServletRequest request) {
        Set<GrantedAuthority> roles = newHashSet();
        roles.add(new SimpleGrantedAuthority(ROLE_USER));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), roles);
        token.setDetails(new WebAuthenticationDetails(request));
        request.getSession();
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
