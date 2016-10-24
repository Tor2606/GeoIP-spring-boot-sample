package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import com.cbinfo.repository.UserDataRepository;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.*;
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
    private static final String URL_ADDRESS = "http://www.ipinfo.io/";
    private static final String DATA_FROM_URL = "Data";

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private LoadingCache<String, String> countryCashMock;

    @Mock
    private UserDataRepository userDataRepositoryMock;

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private UserDataService userDataService;

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
        UserDataService spyUserDataService = Mockito.spy(UserDataService.class);
        doNothing().when(spyUserDataService).setUserDataDTOCountry(any(), any());
        doNothing().when(spyUserDataService).setUserDataDTOFields(any(), any());
        doNothing().when(spyUserDataService).saveUserData(any());

        UserDataDTO actual = spyUserDataService.getDataAndSave(httpServletRequestMock);

        assertThat(actual, is(notNullValue()));
        verify(spyUserDataService, times(1)).setUserDataDTOCountry(any(), any());
        verify(spyUserDataService, times(1)).setUserDataDTOFields(any(), any());
        verify(spyUserDataService, times(1)).saveUserData(any());
        verify(spyUserDataService, times(1)).getDataAndSave(httpServletRequestMock);
        verifyNoMoreInteractions(spyUserDataService);
    }

    @Test
    public void setUserDataDTOCountryTest() throws ExecutionException {
        UserDataDTO actual = new UserDataDTO();

        userDataService.setUserDataDTOCountry(actual, httpServletRequestMock);

        assertThat(actual.getCountry(), is(COUNTRY));
        verify(countryCashMock, times(1)).get(IP_FOR_TESTS);
        verifyNoMoreInteractions(countryCashMock);
    }

    @Test
    public void setUserDataDTOCountryWhenCachingThrowsExceptionTest() throws ExecutionException {
        doThrow(new ExecutionException(new Exception())).when(countryCashMock).get(any());
        UserDataDTO actual = new UserDataDTO();

        userDataService.setUserDataDTOCountry(actual, httpServletRequestMock);

        assertThat(actual.getCountry(), is(UNDEFINED_COUNTRY));
        verify(countryCashMock, times(1)).get(IP_FOR_TESTS);
        verifyNoMoreInteractions(countryCashMock);
    }

    @Test
    public void getCountryTest() throws IOException {
        UserDataService spyUserDataService = Mockito.spy(UserDataService.class);
        doReturn(false).when(spyUserDataService).checkLocalHost(IP_FOR_TESTS);
        doReturn(URL_ADDRESS).when(spyUserDataService).getServiceURL(IP_FOR_TESTS);
        doReturn(DATA_FROM_URL).when(spyUserDataService).readStringFromInputStream(any());
        doReturn(COUNTRY).when(spyUserDataService).getCountryFromJSON(DATA_FROM_URL);

        String actual = spyUserDataService.getCountry(IP_FOR_TESTS);

        assertThat(actual, is(COUNTRY));
        verify(spyUserDataService, times(1)).checkLocalHost(IP_FOR_TESTS);
        verify(spyUserDataService, times(1)).getServiceURL(IP_FOR_TESTS);
        verify(spyUserDataService, times(1)).readStringFromInputStream(any());
        verify(spyUserDataService, times(1)).getCountryFromJSON(DATA_FROM_URL);
        verify(spyUserDataService, times(1)).getCountry(IP_FOR_TESTS);
        verifyNoMoreInteractions(spyUserDataService);
    }

    @Test
    public void getCountryTestWhenLocalHost() throws UnknownHostException {
        UserDataService spyUserDataService = Mockito.spy(UserDataService.class);
        doReturn(true).when(spyUserDataService).checkLocalHost(LOCAL_IP);

        String actual = spyUserDataService.getCountry(LOCAL_IP);

        assertThat(actual, is("localhost"));
        verify(spyUserDataService, times(1)).checkLocalHost(LOCAL_IP);
        verify(spyUserDataService, times(1)).getCountry(LOCAL_IP);
        verifyNoMoreInteractions(spyUserDataService);
    }

    @Test
    public void getCountryWhenErrorTest() throws IOException {
        UserDataService spyUserDataService = Mockito.spy(UserDataService.class);
        doReturn(false).when(spyUserDataService).checkLocalHost(IP_FOR_TESTS);
        doReturn(URL_ADDRESS).when(spyUserDataService).getServiceURL(IP_FOR_TESTS);
        doThrow(new IOException()).when(spyUserDataService).readStringFromInputStream(any());

        String actual = spyUserDataService.getCountry(IP_FOR_TESTS);

        assertThat(actual, is(UNDEFINED_COUNTRY));
        verify(spyUserDataService, times(1)).checkLocalHost(IP_FOR_TESTS);
        verify(spyUserDataService, times(1)).getServiceURL(IP_FOR_TESTS);
        verify(spyUserDataService, times(1)).readStringFromInputStream(any());
        verify(spyUserDataService, times(1)).getCountry(IP_FOR_TESTS);
        verifyNoMoreInteractions(spyUserDataService);
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
    
    @Test
    public void findAllTest() {
        List<UserData> expected = newArrayList(new UserData());
        when(userDataRepositoryMock.findAll()).thenReturn(expected);

        List<UserData> actual = userDataService.findAll();

        assertThat(actual, is(expected));
        assertThat(actual.get(0), is(expected.get(0)));
    }

    @Test
    public void saveUserDataTest() {
        UserDataService spyUserDataService = Mockito.spy(UserDataService.class);
        spyUserDataService.userDataRepository = userDataRepositoryMock;
        UserData userData = new UserData();
        UserDataDTO userDataDTO = new UserDataDTO();
        doReturn(userData).when(spyUserDataService).createUserData(userDataDTO);
        doReturn(userData).when(userDataRepositoryMock).save(userData);

        spyUserDataService.saveUserData(userDataDTO);

        verify(userDataRepositoryMock, times(1)).save(userData);
        verifyNoMoreInteractions(userDataRepositoryMock);
        verify(spyUserDataService, times(1)).createUserData(userDataDTO);
        verify(spyUserDataService, times(1)).saveUserData(userDataDTO);
        verifyNoMoreInteractions(spyUserDataService);
    }

    @Test
    public void getFirstUserWithSameIpWhenThereIsNoUserWithThisIpTest() {
        List<User> list = newArrayList();
        when(userServiceMock.findByUserIp(anyString())).thenReturn(list);

        User actual = userDataService.getFirstUserWithSameIp(new UserDataDTO());

        assertThat(actual, is(nullValue()));
    }

    @Test
    public void getFirstUserWithSameIpWhenThereIsOneUserWithThisIpTest() {
        User expected = new User();
        List<User> list = newArrayList(expected);
        when(userServiceMock.findByUserIp(anyString())).thenReturn(list);

        User actual = userDataService.getFirstUserWithSameIp(new UserDataDTO());

        assertThat(actual, is(expected));
    }

    @Test
    public void getFirstUserWithSameIpWhenThereIsCoupleUserWithThisIpTest() {
        // return (userService.findByUserIp(userDataDTO.getIp()).size() >= 1) ? userService.findByUserIp(userDataDTO.getIp()).get(0) : null;
        User expected = new User();
        User user = new User();
        List<User> list = newArrayList(expected, user);
        when(userServiceMock.findByUserIp(anyString())).thenReturn(list);

        User actual = userDataService.getFirstUserWithSameIp(new UserDataDTO());

        assertThat(actual, is(expected));
    }
}
