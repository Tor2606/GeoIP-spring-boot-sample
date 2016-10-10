package com.cbinfo.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserSessionServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserSessionService userSessionService;

    @Before
    private void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserTest(){

    }

    @Test
    public void setUserIfNotNullTest(){}

    @Test
    public void findAndSetUserFromPrincipalTest(){

    }

    @Test
    public void getPrincipalFromSecurityContextHolderTest(){

    }
}
