package com.cbinfo.service;

import com.cbinfo.listeners.LoginLogoutListener;
import com.cbinfo.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Service
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private static final Log LOG = LogFactory.getLog(LoginLogoutListener.class);

    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        LOG.info("Inside CustomUserDetailsService");
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Wrong username");
        }

        Set<GrantedAuthority> roles = newHashSet();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }
}
