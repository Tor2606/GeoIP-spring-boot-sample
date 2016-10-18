package com.cbinfo.controller;

import com.cbinfo.model.User;
import com.cbinfo.service.UserSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTests {
    private static final String LOGIN_VIEW = "login/login";
    private static final String REDIRECT_TO_APP_HANDLER = "redirect:app";

    private MockMvc mockMvc;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserSessionService userSessionService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void getLoginTestWhenNotLoggedIn() throws Exception {
        when(userSessionService.getUser()).thenReturn(null);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(LOGIN_VIEW))
                .andExpect(forwardedUrl(LOGIN_VIEW));

        verify(userSessionService, times(1)).getUser();
        verifyNoMoreInteractions(userSessionService);
    }

    @Test
    public void getLoginTestWhenLoggedIn() throws Exception {
        when(userSessionService.getUser()).thenReturn(new User());

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_TO_APP_HANDLER))
                .andExpect(redirectedUrl("app"));

        verify(userSessionService, times(1)).getUser();
        verifyNoMoreInteractions(userSessionService);
    }
}
