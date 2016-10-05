package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.model.UserData;
import com.cbinfo.repository.UserDataRepository;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserDataServiceTests {

    private static final String IP_FOR_TESTS = "93.75.87.81";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.92 Safari/537.36";
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String RESULT_TEXT = "result";
    private static final String UNDEFINED_COUNTRY = "undefined";
    private static final String COUNTRY = "CH";

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private LoadingCache<String, String> countryCashMock;

    @Mock
    private UserDataRepository userDataRepositoryMock;

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private UserDataService userDataService = new UserDataService();

    @Before
    public void init() throws ExecutionException {
        MockitoAnnotations.initMocks(this);
        when(httpServletRequestMock.getRemoteAddr()).thenReturn(IP_FOR_TESTS);
        when(httpServletRequestMock.getHeader("User-Agent"))
                .thenReturn(USER_AGENT);
        when(userDataRepositoryMock.save((UserData) any())).thenReturn(null);
        when(userServiceMock.findByUserIp(anyString())).thenReturn(new ArrayList<>());
        when(countryCashMock.get(any())).thenReturn(COUNTRY);
    }

    @Test
    public void getDataAndSaveTest() throws Exception {
        UserDataDTO actual = userDataService.getDataAndSave(httpServletRequestMock);

        assertThat(actual.getBrowser(), is("Chrome"));
        assertThat(actual.getCountry(), is(COUNTRY));
        assertThat(actual.getOperatingSystem(), is("Linux"));
        assertThat(actual.getAgentFamily(), is("Chrome"));
        assertThat(actual.getDeviceCategory(), is("Personal computer"));
        assertThat(actual.getProducer(), is("Google Inc."));
        assertThat(actual.getUserAgent(), is(USER_AGENT));

        verify(countryCashMock, times(1)).get(any());
        verify(userDataRepositoryMock, times(1)).save((UserData) any());
        verify(userServiceMock, times(1)).findByUserIp(anyString());
    }

    @Test
    public void setUserDataDTOCountryTest() throws ExecutionException {
        UserDataDTO actual = new UserDataDTO();
        userDataService.setUserDataDTOCountry(actual,httpServletRequestMock);

        assertThat(actual.getCountry(), is(COUNTRY));
    }

    @Test
    public void setUserDataDTOCountryWhenCachingThrowsExceptionTest() throws ExecutionException {
        doThrow(new ExecutionException(new Exception())).when(countryCashMock).get(any());

        UserDataDTO actual = new UserDataDTO();
        userDataService.setUserDataDTOCountry(actual,httpServletRequestMock);

        assertThat(actual.getCountry(), is(UNDEFINED_COUNTRY));
        verify(countryCashMock, times(1)).get(any());
    }

    @Test
    public void getCountryTest() throws IOException {
        String actual = userDataService.getCountry(IP_FOR_TESTS);
        assertThat(actual, is("UA"));
    }

    @Test
    public void getCountryWhenErrorTest() throws UnknownHostException {
        String actual = userDataService.getCountry("No ip");
        assertThat(actual, is(UNDEFINED_COUNTRY));
    }

    @Test
    public void getCountryFromJSONTest() {
        String actual = userDataService.getCountryFromJSON("{\"country\":\"CH\"}");
        assertThat(actual, is(COUNTRY));
    }

    @Test
    public void setUserDataDTOFieldsTest() throws IOException {
        UserDataDTO actual = new UserDataDTO();

        userDataService.setUserDataDTOFields(actual, httpServletRequestMock);

        assertThat(actual.getBrowser(), is("Chrome"));
        assertThat(actual.getOperatingSystem(), is("Linux"));
        assertThat(actual.getAgentFamily(), is("Chrome"));
        assertThat(actual.getDeviceCategory(), is("Personal computer"));
        assertThat(actual.getProducer(), is("Google Inc."));
        assertThat(actual.getUserAgent(), is(USER_AGENT));
    }

    @Test
    public void readStringFromInputStreamTest() throws IOException {
        InputStream inputStreamMock = IOUtils.toInputStream(RESULT_TEXT);
        String actual = userDataService.readStringFromInputStream(inputStreamMock);

        assertThat(actual, is(RESULT_TEXT));
    }

    @Test
    public void checkLocalHostNotLocalIpTest() throws UnknownHostException {
        Boolean actual = userDataService.checkLocalHost(IP_FOR_TESTS);
        assertThat(actual, is(false));
    }

    @Test
    public void checkLocalHostWhenLocalIpTest() throws UnknownHostException {
        Boolean actual = userDataService.checkLocalHost(LOCAL_IP);
        assertThat(actual, is(true));
    }
}
