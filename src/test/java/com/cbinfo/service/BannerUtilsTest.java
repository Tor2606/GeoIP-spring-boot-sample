package com.cbinfo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.cbinfo.utils.WebsiteUtils.checkUrl;

@RunWith(MockitoJUnitRunner.class)
public class BannerUtilsTest {

    @Test
    public void checkUrlTest(){
        assert(checkUrl("ee2Et.ry"));
        assert(!checkUrl("..we"));
        assert(!checkUrl("1EEer."));
    }
}
