package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.model.enums.UserRoles;
import com.cbinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    protected PasswordEncoder passwordEncoder;

    public void createUser(UserForm userForm, String ip) {
        User user = new User();
        user.setUserIp(ip);
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRole(UserRoles.ROLE_USER);
        saveUser(user);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findByUserIp(String userIp) {
        return userRepository.findByUserIp(userIp);
    }

    public boolean isEmailRegistered(String email) {
        return findByEmail(email) != null;
    }

    public void updateCurrentUser(UserForm userForm, String reenteredPassword) throws Exception {
        User userFromDB = findByEmail(getUserPrincipalLogin());
        checkEmailOnUpdating(userForm, userFromDB);
        checkReenteredPassword(userForm.getPassword(),reenteredPassword);
        updateUserFields(userForm, userFromDB);
        saveUser(userFromDB);
        userSessionService.setUser(userFromDB);
    }

    public void checkReenteredPassword(String password, String reenteredPassword) throws Exception {
        if (!password.equals(reenteredPassword)) {
            throw new Exception("Password and reentered password are different!");
        }
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

    protected String getUserPrincipalLogin() {
        if (null != getUserPrincipals()) {
            return getUserPrincipals().getUsername();
        }
        return null;
    }

    protected org.springframework.security.core.userdetails.User getUserPrincipals() {
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
        UserForm result = new UserForm();
        result.setEmail(user.getEmail());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        return result;
    }

    @Transactional
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
