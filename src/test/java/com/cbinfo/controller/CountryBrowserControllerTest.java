package com.cbinfo.controller;


import com.cbinfo.dto.UserDataDto;
import com.cbinfo.service.UserDataService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Igor on 10.08.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class CountryBrowserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CountryBrowserController countryBrowserController;

    @Mock
    private UserDataService userDataService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryBrowserController).build();
    }

    @Test
    public void getCountryBrowserDataTest() throws Exception {
        UserDataDto userDataDto = createUserDataDto();
        when(userDataService.getData(any())).thenReturn(userDataDto);
        mockMvc.perform(get("/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.browser", is(userDataDto.getBrowser())))
                .andExpect(jsonPath("$.operationalSystem", is(userDataDto.getOperatingSystem())))
                .andExpect(jsonPath("$.userAgent", is(userDataDto.getUserAgent())))
                .andExpect(jsonPath("$.country", is(userDataDto.getCountry())));
    }

    private UserDataDto createUserDataDto() {
        UserDataDto result = new UserDataDto();
        result.setBrowser("Chrome");
        result.setOperatingSystem("Linux Mint");
        result.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        result.setCountry("ua");
        return result;
    }

}
