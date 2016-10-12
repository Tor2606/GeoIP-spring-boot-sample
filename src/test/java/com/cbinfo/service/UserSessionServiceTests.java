package com.cbinfo.service;

import com.cbinfo.model.User;
import com.google.common.base.Joiner;
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

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserSessionServiceTests {

    private static final String USERNAME = "username";
    @Mock
    private UserService userService;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @InjectMocks
    private UserSessionService userSessionService;

    @Mock
    private org.springframework.security.core.userdetails.User userDetailsUserMock;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserTest(){
        UserSessionService spyUserSessionService = Mockito.spy(UserSessionService.class);
        User user = new User();
        spyUserSessionService.user = user;
        doNothing().when(spyUserSessionService).setUserIfNull();

        User actual = spyUserSessionService.getUser();

        assertThat(actual, is(user));
        verify(spyUserSessionService, times(1)).setUserIfNull();
        verify(spyUserSessionService, times(1)).getUser();
        verifyNoMoreInteractions(spyUserSessionService);
    }

    @Test
    public void setUserIfNotNullWhenUserIsNotNullTest(){
        UserSessionService spyUserSessionService = Mockito.spy(UserSessionService.class);
        spyUserSessionService.user = new User();

        spyUserSessionService.setUserIfNull();

        verify(spyUserSessionService, never()).getPrincipalFromSecurityContextHolder();
    }

    @Test
    public void setUserIfNotNullWhenUserIsNullTest(){
        UserSessionService spyUserSessionService = Mockito.spy(UserSessionService.class);
        spyUserSessionService.user = null;
        org.springframework.security.core.userdetails.User principal = Mockito.mock(org.springframework.security.core.userdetails.User.class);
        doReturn(principal).when(spyUserSessionService).getPrincipalFromSecurityContextHolder();
        doNothing().when(spyUserSessionService).findAndSetUserFromPrincipal(principal);

        spyUserSessionService.setUserIfNull();

        verify(spyUserSessionService, times(1)).getPrincipalFromSecurityContextHolder();
        verify(spyUserSessionService, times(1)).findAndSetUserFromPrincipal(principal);
        verify(spyUserSessionService, times(1)).setUserIfNull();
        verifyNoMoreInteractions(spyUserSessionService);
    }

    @Test
    public void findAndSetUserFromPrincipalWhenPrincipalIsNotNullTest(){
        org.springframework.security.core.userdetails.User principal = Mockito.mock(org.springframework.security.core.userdetails.User.class);
        User user = new User();
        when(principal.getUsername()).thenReturn(USERNAME);
        when(userService.findByEmail(USERNAME)).thenReturn(user);

        userSessionService.findAndSetUserFromPrincipal(principal);

        assertThat(userSessionService.getUser(), is(user));
        verify(userService, times(1)).findByEmail(USERNAME);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findAndSetUserFromPrincipalWhenPrincipalIsNullTest(){
        org.springframework.security.core.userdetails.User principal = null;

        userSessionService.findAndSetUserFromPrincipal(principal);

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getPrincipalFromSecurityContextHolderTest(){
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetailsUserMock,null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User actual = userSessionService.getPrincipalFromSecurityContextHolder();

        assertThat(actual, is(userDetailsUserMock));
    }

    @Test
    public void getPrincipalFromSecurityContextHolderWhenClassCastExceptionTest(){
        Authentication auth = new UsernamePasswordAuthenticationToken("ClassCastException invoker",null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User actual = userSessionService.getPrincipalFromSecurityContextHolder();

        assertThat(actual, is(nullValue()));
    }
}
