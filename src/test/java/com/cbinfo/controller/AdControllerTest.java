package com.cbinfo.controller;

import com.cbinfo.model.Banner;
import com.cbinfo.service.BannerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(MockitoJUnitRunner.class)
public class AdControllerTest {

    private static final String BANNER_ID = "11";
    private MockMvc mockMvc;

    @Mock
    private BannerService bannerServiceMock;

    @InjectMocks
    private AdController adController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
    }

    @Test
    public void getBannerTest() throws Exception {
        Banner banner = new Banner();
        banner.setTitle("title");
        banner.setUrl("url.com");
        when(bannerServiceMock.findBanner(BANNER_ID)).thenReturn(banner);
        mockMvc.perform(get("/ad/script/{bannerId}", BANNER_ID)).andDo(print());
    }
}
