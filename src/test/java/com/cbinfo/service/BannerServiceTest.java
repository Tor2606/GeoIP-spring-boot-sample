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
        assert(bannerService.checkUrl("www.eet.ry"));
        assert(!bannerService.checkUrl("www..we"));
        assert(!bannerService.checkUrl("www.er."));
    }
}
