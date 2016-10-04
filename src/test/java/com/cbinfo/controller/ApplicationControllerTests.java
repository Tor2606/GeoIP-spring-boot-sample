package com.cbinfo.controller;

import com.cbinfo.service.UserDataService;
import com.cbinfo.service.UserSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private UserDataService userDataService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
    }

    @Test
    public void getMainPageTest() throws Exception {
        mockMvc.perform(get("/app"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
