package com.cbinfo.service;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.model.User;
import com.cbinfo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
    private static final String USERNAME = "Username";
    public static final String ENCODED_PASSWORD = "Encoded";
    private static final String COMPANY_NAME = "Company name";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private CompanyService companyService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(passwordEncoder.encode(any())).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    public void createUserTest() {
        UserForm userForm = getUserForm();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.passwordEncoder = passwordEncoder;
        spyUserService.companyService = companyService;
        spyUserService.authenticationService = authenticationService;
        doNothing().when(spyUserService).saveUser(any());

        spyUserService.createUser(userForm, request);

        verify(spyUserService, times(1)).saveUser(any());
        verify(spyUserService, times(1)).createUser(any(), any());
        verifyNoMoreInteractions(spyUserService);

        verify(companyService, times(1)).getCompanyByName(userForm.getCompanyName());
        verifyNoMoreInteractions(companyService);

        verify(authenticationService, times(1)).authenticate(userForm, request);
        verifyNoMoreInteractions(authenticationService);

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
        String reenteredPass = PASSWORD;
        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.userSessionService = Mockito.mock(UserSessionService.class);

        doReturn(USERNAME).when(spyUserService).getUserPrincipalLogin();
        doReturn(userFromDB).when(spyUserService).findByEmail(USERNAME);
        doNothing().when(spyUserService).checkEmailOnUpdating(userForm, userFromDB);
        doNothing().when(spyUserService).checkReenteredPassword(anyString(), anyString());
        doNothing().when(spyUserService).updateUserCompany(userForm, userFromDB);
        doNothing().when(spyUserService).updateUserFields(userForm, userFromDB);
        doNothing().when(spyUserService).saveUser(userFromDB);
        doNothing().when(spyUserService.userSessionService).setUser(userFromDB);

        spyUserService.updateCurrentUser(userForm, reenteredPass);

        verify(spyUserService, times(1)).getUserPrincipalLogin();
        verify(spyUserService, times(1)).findByEmail(USERNAME);
        verify(spyUserService, times(1)).checkEmailOnUpdating(userForm, userFromDB);
        verify(spyUserService, times(1)).updateUserFields(userForm, userFromDB);
        verify(spyUserService, times(1)).saveUser(userFromDB);
        verify(spyUserService, times(1)).checkReenteredPassword(anyString(), anyString());
        verify(spyUserService, times(1)).updateCurrentUser(userForm, reenteredPass);
        verify(spyUserService, times(1)).updateUserCompany(userForm, userFromDB);
        verifyNoMoreInteractions(spyUserService);

        verify(spyUserService.userSessionService, times(1)).setUser(userFromDB);
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
        verify(spyUserService, times(1)).checkEmailOnUpdating(userForm, user);
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
        verify(spyUserService, times(1)).checkEmailOnUpdating(userForm, user);
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
        verify(spyUserService, times(1)).checkEmailOnUpdating(userForm, user);
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void getUserPrincipalLoginTest() {
        org.springframework.security.core.userdetails.User userDetailsUserMock = Mockito.mock(org.springframework.security.core.userdetails.User.class);
        when(userDetailsUserMock.getUsername()).thenReturn(USERNAME);
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(userDetailsUserMock).when(spyUserService).getUserPrincipals();

        String actual = spyUserService.getUserPrincipalLogin();

        assertThat(actual, is(USERNAME));
        verify(userDetailsUserMock, times(1)).getUsername();
        verifyNoMoreInteractions(userDetailsUserMock);
        verify(spyUserService, times(2)).getUserPrincipals();
        verify(spyUserService, times(1)).getUserPrincipalLogin();
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void getUserPrincipalLoginWhenUserPrincipalsIsNullTest() {
        UserService spyUserService = Mockito.spy(UserService.class);
        doReturn(null).when(spyUserService).getUserPrincipals();

        String actual = spyUserService.getUserPrincipalLogin();

        assertThat(actual, is(nullValue()));
        verify(spyUserService, times(1)).getUserPrincipals();
        verify(spyUserService, times(1)).getUserPrincipalLogin();
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void getUserPrincipalsOKTest() {
        org.springframework.security.core.userdetails.User userDetailsUserMock =
                Mockito.mock(org.springframework.security.core.userdetails.User.class);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetailsUserMock, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User actual = userService.getUserPrincipals();

        assertThat(actual, is(userDetailsUserMock));
    }

    @Test
    public void getUserPrincipalsWhenPrincipalsAreInstanceOfStringTest() {
        Authentication auth = new UsernamePasswordAuthenticationToken("Some String instance", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User actual = userService.getUserPrincipals();

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getUserPrincipalsWhenAuthIsNullTest() {
        Authentication auth = null;
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User actual = userService.getUserPrincipals();

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void updateUserFieldsTest() {
        UserForm userForm = getUserForm();
        User user = new User();

        userService.updateUserFields(userForm, user);

        assertThat(user.getPassword(), is(ENCODED_PASSWORD));
        assertThat(user.getFirstName(), is(userForm.getFirstName()));
        assertThat(user.getLastName(), is(userForm.getLastName()));
    }

    @Test
    public void updateUserFieldsWhenPasswordIsBlankTest() {
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        UserForm userForm = getUserForm();
        userForm.setPassword("");
        User user = new User();

        userService.updateUserFields(userForm, user);

        assertThat(user.getPassword(), is(nullValue()));
        assertThat(user.getFirstName(), is(userForm.getFirstName()));
        assertThat(user.getLastName(), is(userForm.getLastName()));
    }

    @Test
    public void updateUserFieldsWhenFirstNameIsBlankTest() {
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        UserForm userForm = getUserForm();
        userForm.setFirstName(null);
        User user = new User();

        userService.updateUserFields(userForm, user);

        assertThat(user.getPassword(), is(ENCODED_PASSWORD));
        assertThat(user.getFirstName(), is(nullValue()));
        assertThat(user.getLastName(), is(userForm.getLastName()));
    }

    @Test
    public void updateUserFieldsWhenLastNameIsBlankTest() {
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        UserForm userForm = getUserForm();
        userForm.setLastName(null);
        User user = new User();

        userService.updateUserFields(userForm, user);

        assertThat(ENCODED_PASSWORD, is(user.getPassword()));
        assertThat(user.getFirstName(), is(userForm.getFirstName()));
        assertThat(user.getLastName(), is(nullValue()));
    }


    @Test
    public void getCurrentSessionUserToFormTest() {
        UserService spyUserService = Mockito.spy(UserService.class);
        spyUserService.userSessionService = userSessionService;
        User user = new User();
        UserForm userForm = new UserForm();
        doReturn(user).when(userSessionService).getUser();
        doReturn(userForm).when(spyUserService).userFormSetter(user);

        UserForm actual = spyUserService.getCurrentSessionUserToForm();

        assertThat(actual, is(userForm));
        verify(spyUserService, times(1)).userFormSetter(user);
        verify(spyUserService, times(1)).getCurrentSessionUserToForm();
        verifyNoMoreInteractions(spyUserService);
    }

    @Test
    public void userFormSetterTest() {
        User user = getUser();

        UserForm actual = userService.userFormSetter(user);

        assertThat(actual.getFirstName(), is(user.getFirstName()));
        assertThat(actual.getLastName(), is(user.getLastName()));
        assertThat(actual.getEmail(), is(user.getEmail()));
    }

    @Test
    public void findAllTest() {
        List<User> list = newArrayList(getUser());
        when(userRepository.findAll()).thenReturn(list);

        List actual = userService.findAll();

        assertThat(actual, is(list));
        assertThat(actual.get(0), is(list.get(0)));
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void saveUserTest() {
        User user = getUser();

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findByEmailTest() {
        User user = getUser();
        when(userRepository.findOneByEmail(EMAIL_VALUE)).thenReturn(user);

        User actual = userService.findByEmail(EMAIL_VALUE);

        assertThat(actual, is(user));
        verify(userRepository, times(1)).findOneByEmail(EMAIL_VALUE);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findByUserIpTest() {
        List<User> list = newArrayList(getUser());
        when(userRepository.findByUserIp(EMAIL_VALUE)).thenReturn(list);

        List actual = userService.findByUserIp(EMAIL_VALUE);

        assertThat(actual, is(list));
        assertThat(actual.get(0), is(list.get(0)));
        verify(userRepository, times(1)).findByUserIp(EMAIL_VALUE);
        verifyNoMoreInteractions(userRepository);
    }

    private UserForm getUserForm() {
        UserForm result = new UserForm();
        result.setFirstName(FIRST_NAME);
        result.setLastName(LAST_NAME);
        result.setEmail(EMAIL_VALUE);
        result.setPassword(PASSWORD);
        result.setCompanyName(COMPANY_NAME);
        return result;
    }

    private User getUser() {
        User result = new User();
        result.setFirstName(FIRST_NAME);
        result.setLastName(LAST_NAME);
        result.setEmail(EMAIL_VALUE);
        return result;
    }
}
