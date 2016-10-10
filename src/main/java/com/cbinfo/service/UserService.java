package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    protected UserSessionService userSessionService;

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
    protected void saveUser(User user) {
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
        return findByEmail(email) != null;
    }

    public void updateCurrentUser(UserForm userForm) throws Exception {
        User userFromDB = findByEmail(getUserPrincipalLogin());
        checkEmailOnUpdating(userForm, userFromDB);
        updateUserFields(userForm, userFromDB);
        saveUser(userFromDB);
        userSessionService.setUser(userFromDB);
    }

    protected void checkEmailOnUpdating(UserForm userForm, User userFromDB) throws Exception {
        String userFormEmail = userForm.getEmail();
        if (!isBlank(userFormEmail) && !userFromDB.getEmail().equals(userFormEmail)) {
            if (findByEmail(userFormEmail) == null) {
                userFromDB.setEmail(userFormEmail);
            } else {
                throw new Exception("User with this email is registered");
            }
        }
    }

    public static String getUserPrincipalLogin() {
        if (null != getUserPrincipals()) {
            return getUserPrincipals().getUsername();
        }
        return null;
    }

    protected static org.springframework.security.core.userdetails.User getUserPrincipals() {
        if (null != SecurityContextHolder.getContext().getAuthentication()) {
            if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
                return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }
        }
        return null;
    }

    protected void updateUserFields(UserForm userForm, User userFromDB) {
        if (isNotBlank(userForm.getPassword())) {
            userFromDB.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }
        if (isNotBlank(userForm.getFirstName())) {
            userFromDB.setFirstName(userForm.getFirstName());
        }
        if (isNotBlank(userForm.getLastName())) {
            userFromDB.setLastName(userForm.getLastName());
        }
    }

    public UserForm getCurrentSessionUserToForm() {
        User u = userSessionService.getUser();
        return userFormSetter(u);
    }

    protected UserForm userFormSetter(User user){
        UserForm userForm = new UserForm();
        userForm.setEmail(user.getEmail());
        userForm.setPassword(user.getPassword());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());
        return userForm;
    }
}
