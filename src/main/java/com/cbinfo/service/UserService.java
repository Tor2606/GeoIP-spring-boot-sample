package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by islabukhin on 21.09.16.
 */
@Service
public class UserService {

    private static final Log LOGGER = LogFactory.getLog(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(UserForm userForm, String ip) {
        User user = new User();
        user.setUserIp(ip);
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        saveUser(user);
    }

    @Transactional
    private void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findByUserIp(String userIp) {
        return userRepository.findByUserIp(userIp);
    }

    public boolean isEmailRegistered(String email) {
        return findByEmail(email) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    public void updateCurrentUser(UserForm userForm) throws Exception {
        User userFromDB = findByEmail(getUserPrincipalLogin());
        String userFormEmail = userForm.getEmail();
        if (!isBlank(userFormEmail) && !userFromDB.getEmail().equals(userFormEmail)) {
            if (findByEmail(userFormEmail) == null) {
                userFromDB.setEmail(userFormEmail);
            } else {
                throw new Exception("User with this email is registered");
            }
        }
        updateUserFields(userForm, userFromDB);
        saveUser(userFromDB);
    }



    public static String getUserPrincipalLogin() {
        if (null != getUserPrincipals()) {
            return getUserPrincipals().getUsername();
        }
        return null;
    }

    private static org.springframework.security.core.userdetails.User getUserPrincipals() {
        if (null != SecurityContextHolder.getContext().getAuthentication()) {
            if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
                return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
        }
        return null;
    }

    private void updateUserFields(UserForm userForm, User userFromDB) {
        if (null != userForm.getEmail()) {
            userFromDB.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }
        if (null != userForm.getFirstName()) {
            userFromDB.setFirstName(userForm.getFirstName());
        }
        if (isNotBlank(userForm.getLastName())) {
            userFromDB.setLastName(userForm.getLastName());
        }
    }
}
