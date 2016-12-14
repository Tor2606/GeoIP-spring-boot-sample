package com.cbinfo.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BannerServiceTest {

    @InjectMocks
    private BannerService bannerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkUrlTest(){
        assert(bannerService.checkUrl("ee2Et.ry"));
        assert(!bannerService.checkUrl("..we"));
        assert(!bannerService.checkUrl("1EEer."));
    }
}
