package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by islabukhin on 21.09.16.
 */
@Service
public class UserService {
    private static final Log LOGGER = LogFactory.getLog(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(UserForm userForm, String ip){
        User user = new User();
        user.setUserIp(ip);
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(user);
        return;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserIp(String userIp){return userRepository.findByUserIp(userIp);}

    public boolean isEmailRegistered(String email) {
        return findByEmail(email) == null ? Boolean.FALSE : Boolean.TRUE;
    }
}
