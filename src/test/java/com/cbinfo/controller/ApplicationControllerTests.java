package com.cbinfo.controller;

import com.cbinfo.model.*;
import com.cbinfo.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTests {
    private static final String MAIN_VIEW = "application/app";

    private MockMvc mockMvc;

    private User userMock;

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private UserService userService;

    @Mock
    private CompanyService companyService;

    @Mock
    private CampaignService campaignService;

    @Mock
    private WebsiteService websiteService;

    @Mock
    private UserDataService userDataService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
        userMock = setUserMock();
        when(userDataService.findAll()).thenReturn(getUserDataListMock());
        when(userSessionService.getUser()).thenReturn(userMock);
        when(userService.findAll()).thenReturn(getUserListMock());
        when(companyService.findAll()).thenReturn(getCompanyMock());
        when(campaignService.findAllCurrentUserCampaigns()).thenReturn(getCampaignsMock());
        when(websiteService.findAll()).thenReturn(getWebsitesMock());
    }

    private List<Website> getWebsitesMock() {
        return newArrayList(new Website(), new Website());
    }

    private List<Company> getCompanyMock() {
        return newArrayList(new Company(), new Company());
    }

    private List<Campaign> getCampaignsMock() {
        return newArrayList(new Campaign(),new Campaign());
    }

    @Test
    public void getMainPageTest() throws Exception {
        mockMvc.perform(get("/app"))
                .andExpect(status().isOk())
                .andExpect(view().name(MAIN_VIEW))
                .andExpect(forwardedUrl(MAIN_VIEW))
                .andExpect(model().attribute("userDataList", hasSize(2)))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("companies", hasSize(2)))
                .andExpect(model().attribute("campaigns", hasSize(2)))
                .andExpect(model().attribute("websites", hasSize(2)))
                .andExpect(model().attribute("user", is(userMock)));
        verify(userSessionService, times(1)).getUser();
        verifyNoMoreInteractions(userSessionService);
        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
        verify(userDataService, times(1)).findAll();
        verifyNoMoreInteractions(userDataService);
        verify(companyService, times(1)).findAll();
        verifyNoMoreInteractions(companyService);
        verify(campaignService, times(1)).findAllCurrentUserCampaigns();
        verifyNoMoreInteractions(campaignService);
        verify(websiteService, times(1)).findAll();
        verifyNoMoreInteractions(websiteService);
    }

    private List<UserData> getUserDataListMock(){
        return newArrayList(new UserData(), new UserData());
    }

    private List<User> getUserListMock(){return newArrayList(new User(), new User());}

    private User setUserMock() {
        return new User();
    }
}
