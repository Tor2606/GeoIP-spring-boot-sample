package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by islabukhin on 21.09.16.
 */
@Service
public class UserService {
    private static final Log LOGGER = LogFactory.getLog(UserService.class);

    @Autowired
    UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
