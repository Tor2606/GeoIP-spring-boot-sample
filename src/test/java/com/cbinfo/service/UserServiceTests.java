package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.model.UserRequest;
import com.cbinfo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    private static final String IP_VALUE = "ip";
    private static final String FIRST_NAME = "Name";
    private static final String LAST_NAME = "Surname";
    private static final String EMAIL_VALUE = "Email";
    private static final String PASSWORD = "password";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionService userSessionService;

    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserTest() {
        UserForm userForm = getUserForm();
        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.createUser(userForm, IP_VALUE);

        verify(spyUserService, times(1)).saveUser(any());
        verify(spyUserService, times(1)).createUser(any(), any());
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void isEmailRegisteredTest() {
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(new User()).when(spyUserService).findByEmail(EMAIL_VALUE);

        Boolean actual = spyUserService.isEmailRegistered(EMAIL_VALUE);

        assertThat(actual, is(Boolean.TRUE));
        verify(spyUserService, times(1)).findByEmail(EMAIL_VALUE);
        verify(spyUserService, times(1)).isEmailRegistered(EMAIL_VALUE);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void isEmailRegisteredNegativeTest() {
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(null).when(spyUserService).findByEmail(EMAIL_VALUE);

        Boolean actual = spyUserService.isEmailRegistered(EMAIL_VALUE);

        assertThat(actual, is(Boolean.FALSE));
        verify(spyUserService, times(1)).findByEmail(EMAIL_VALUE);
        verify(spyUserService, times(1)).isEmailRegistered(EMAIL_VALUE);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void updateCurrentUserTest() throws Exception {
        UserForm userForm = getUserForm();
        User userFromDB = new User();
        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.userSessionService = userSessionService;

        doReturn(userFromDB).when(spyUserService).findByEmail(anyString());
        doNothing().when(spyUserService).checkEmailOnUpdating(userForm, userFromDB);
        doNothing().when(spyUserService).updateUserFields(userForm, userFromDB);
        doNothing().when(spyUserService).saveUser(userFromDB);
        doNothing().when(spyUserService.userSessionService).setUser(userFromDB);

        spyUserService.updateCurrentUser(userForm);

        verify(spyUserService, times(1)).findByEmail(anyString());
        verify(spyUserService, times(1)).checkEmailOnUpdating(userForm,userFromDB);
        verify(spyUserService, times(1)).updateUserFields(userForm,userFromDB);
        verify(spyUserService, times(1)).saveUser(userFromDB);
        verify(spyUserService,times(1)).updateCurrentUser(userForm);
        verifyNoMoreInteractions(spyUserService);

        verify(userSessionService, times(1)).setUser(userFromDB);
        verifyNoMoreInteractions(userSessionService);
    }

    @Test
    public void checkEmailOnUpdatingWhenEmailsInDBAndInFormAreEqualTest() throws Exception {
        UserForm userForm = getUserForm();
        User user = new User();
        user.setEmail(EMAIL_VALUE);

        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.checkEmailOnUpdating(userForm, user);

        verify(spyUserService, never()).findByEmail(anyString());
        verify(spyUserService,times(1)).checkEmailOnUpdating(userForm,user);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void checkEmailOnUpdatingWhenEmailInFormAreEmptyTest() throws Exception {
        UserForm userForm = new UserForm();
        User user = new User();
        user.setEmail(EMAIL_VALUE);

        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.checkEmailOnUpdating(userForm, user);

        verify(spyUserService, never()).findByEmail(anyString());
        verify(spyUserService,times(1)).checkEmailOnUpdating(userForm,user);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test(expected = Exception.class)
    public void checkEmailOnUpdatingWhenEmailIsRegisteredTest() throws Exception {
        UserForm userForm = getUserForm();
        User user = new User();
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(new User()).when(spyUserService).findByEmail(anyString());

        spyUserService.checkEmailOnUpdating(userForm, user);
    }

    @Test
    public void checkEmailOnUpdatingWhenEmailIsNewAcceptableTest() throws Exception {
        UserForm userForm = getUserForm();
        User user = new User();
        user.setEmail("someEmail");
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(null).when(spyUserService).findByEmail(anyString());

        spyUserService.checkEmailOnUpdating(userForm, user);

        assertThat(user.getEmail(), is(userForm.getEmail()));

        verify(spyUserService, times(1)).findByEmail(anyString());
        verify(spyUserService,times(1)).checkEmailOnUpdating(userForm,user);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void getUserPrincipalLoginTest() {

    }

    @Test
    public void getUserPrincipalsTest() {

    }

    @Test
    public void updateUserFieldsTest() {

    }

    @Test
    public void getCurrentSessionUserToFormTest() {

    }

    @Test
    public void userFormSetterTest() {

    }

    private UserForm getUserForm() {
        UserForm userForm = new UserForm();
        userForm.setFirstName(FIRST_NAME);
        userForm.setLastName(LAST_NAME);
        userForm.setEmail(EMAIL_VALUE);
        userForm.setPassword(PASSWORD);
        return userForm;
    }
}
