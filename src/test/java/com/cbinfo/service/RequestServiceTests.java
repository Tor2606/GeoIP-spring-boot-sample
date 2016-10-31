package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequest;
import com.cbinfo.repository.UserRequestsRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceTests {
    //// TODO: 24.10.2016  think on regex for Artem
    private static String propertyFilePath = "src/test/java/com/cbinfo/resources/testing.properties";
    private static Properties testProperties = getProperties(propertyFilePath);

    private static String loggingMessageBeginning = testProperties.getProperty("loggingMessageBeginning");
    private static String timePattern = testProperties.getProperty("timePattern");
    private static final String URI_VALUE = "uri";
    private static final String IP_VALUE = "ip";
    private static final String EMAIL_VALUE = "email";
    private static final String START_TIME = "startTime";
    private static final String ANONYMOUS = "ANONYMOUS";

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private UserRequestsRepository userRequestsRepository;

    @InjectMocks
    private RequestService requestService;

    public static String getLoggingMessageBeginning() {
        return loggingMessageBeginning;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveHttpServletRequestTest() {
        RequestService spyRequestService = spy(RequestService.class);
        doNothing().when(spyRequestService).setRequestModelFields(any(), any());
        doNothing().when(spyRequestService).saveRequestModel(any());

        spyRequestService.saveHttpServletRequest(any());

        Mockito.verify(spyRequestService, times(1)).setRequestModelFields(any(), any());
        Mockito.verify(spyRequestService, times(1)).saveRequestModel(any());
        Mockito.verify(spyRequestService, times(1)).saveHttpServletRequest(any());
        Mockito.verifyNoMoreInteractions(spyRequestService);
    }

    @Test
    public void setRequestModelFieldsTest() {
        UserRequest actual = new UserRequest();
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        User userMock = new User();
        when(requestMock.getRequestURI()).thenReturn(URI_VALUE);
        when(requestMock.getRemoteAddr()).thenReturn(IP_VALUE);
        when(userSessionService.getUser()).thenReturn(userMock);

        requestService.setRequestModelFields(actual, requestMock);

        assertThat(actual.getIp(), is(IP_VALUE));
        assertThat(actual.getUrl(), is(URI_VALUE));
        assertThat(actual.getUser(), is(userMock));
        assert (actual.getTime().getTime() - (new Date().getTime()) < 1000); // Near current date
        Mockito.verify(userSessionService, times(1)).getUser();
        Mockito.verifyNoMoreInteractions(userSessionService);
    }

    @Test
    public void getLoggingMessageTest() {
        HttpServletRequest httpServletRequestMock = Mockito.mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURI()).thenReturn(URI_VALUE);
        when(httpServletRequestMock.getRemoteAddr()).thenReturn(IP_VALUE);
        when(httpServletRequestMock.getAttribute(START_TIME)).thenReturn(System.currentTimeMillis());
        RequestService spyRequestService = Mockito.spy(RequestService.class);
        doReturn(EMAIL_VALUE).when(spyRequestService).getUserEmail();
        String expectedDate = new Date().toString();

        String actual = spyRequestService.getLoggingMessage(httpServletRequestMock);
        assertThat(getBeginning(actual), is(loggingMessageBeginning));
        assertThat(getDate(actual), is(expectedDate));
        assert (actual.contains(IP_VALUE));
        assert (actual.contains(URI_VALUE));
        assert (actual.contains(EMAIL_VALUE));
        assert (getLoadingTime(actual) < 100);
        verify(spyRequestService, times(1)).getUserEmail();
        verify(spyRequestService, times(1)).getLoggingMessage(any());
        verifyNoMoreInteractions(spyRequestService);
    }

    @Test
    public void getUserEmailTest() {
        User user = new User();
        user.setEmail(EMAIL_VALUE);
        when(userSessionService.getUser()).thenReturn(user);

        String actual = requestService.getUserEmail();

        assertThat(actual, is(EMAIL_VALUE));
        verify(userSessionService, times(1)).getUser();
        verifyNoMoreInteractions(userSessionService);
    }

    @Test
    public void getUserEmailWhenNullTest() {
        when(userSessionService.getUser()).thenReturn(null);

        String actual = requestService.getUserEmail();

        assertThat(actual, is(ANONYMOUS));
        verify(userSessionService, times(1)).getUser();
        verifyNoMoreInteractions(userSessionService);
    }

    @Test
    public void saveRequestModelTest() {
        UserRequest userRequest = new UserRequest();

        requestService.saveRequestModel(userRequest);

        verify(userRequestsRepository, times(1)).save((UserRequest) any());
        verifyNoMoreInteractions(userRequestsRepository);
    }

    private int getLoadingTime(String actual) {
        String s = timePattern + "(, ..?,)";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(actual);
        String result = "";
        while (matcher.find()) {
            result = actual.substring(matcher.end() - 3, matcher.end() - 1);
        }
        result = StringUtils.deleteWhitespace(result);
        return Integer.valueOf(result);
    }

    private String getDate(String actual) {
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(actual);
        String result = "";
        while (matcher.find()) {
            result = actual.substring(matcher.start(), matcher.end());
        }
        return result;
    }

    private String getBeginning(String s) {
        return s.split(":")[0].concat(":");
    }

    private static Properties getProperties(String propertyFilePath) {
        Properties properties = new Properties();
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(propertyFilePath));
            properties.load(inputStream);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
