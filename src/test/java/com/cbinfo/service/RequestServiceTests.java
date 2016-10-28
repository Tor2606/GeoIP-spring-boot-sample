package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequest;
import com.cbinfo.repository.UserRequestsRepository;
import com.google.common.base.Joiner;
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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class RequestServiceTests {
    //todo move to prop file (@Value()), how to pass it to mockito(It's possible to get it in Property class(straight and bad)too)
    //todo add deleting users and comp
    //// TODO: 24.10.2016  think on regex for Artem
    //todo don't write request param(name), if it's the same
    //// TODO: 24.10.2016 pass to service new method for company
    //todo change findAll in service(through stream, not straight)
    private static final String LOGGING_MESSAGE_BEGINNING ="Incoming request(time, loading page time in millis, ip, url, users email): ";
    private static final String URI_VALUE = "uri";
    private static final String IP_VALUE = "ip";
    private static final String EMAIL_VALUE = "email";
    private static final String DELIMITER = ", ";
    private static final String START_TIME = "startTime";
    private static final String BASIC_PATTERN = ": ... ... .. ..:..:.. .... ....";
    private static final String ANONYMOUS = "ANONYMOUS";

    @Mock
    private UserSessionService userSessionService;

    @Mock
    private UserRequestsRepository userRequestsRepository;

    @InjectMocks
    private RequestService requestService;

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
        String expectedDate = new Date().toString();//

        String actual = spyRequestService.getLoggingMessage(httpServletRequestMock);
        System.out.println(LOGGING_MESSAGE_BEGINNING);
        assertThat(getBeginning(actual), is(LOGGING_MESSAGE_BEGINNING));
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

    private int getLoadingTime(String actual) {
        String s = BASIC_PATTERN + "(, ..?,)";
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
        Pattern pattern = Pattern.compile(BASIC_PATTERN);
        Matcher matcher = pattern.matcher(actual);
        String result = "";
        while (matcher.find()) {
            result = actual.substring(matcher.start() + 2, matcher.end());
        }
        return result;
    }

    private String getBeginning(String s) {
        return s.split(":")[0].concat(": ");
    }

    private String expectedMessage(String date) {
        return LOGGING_MESSAGE_BEGINNING + Joiner.on(DELIMITER)
                .join(date, 0L, IP_VALUE, URI_VALUE, EMAIL_VALUE);
    }

    @Test
    public void saveRequestModelTest() {
        UserRequest userRequest = new UserRequest();

        requestService.saveRequestModel(userRequest);

        verify(userRequestsRepository, times(1)).save((UserRequest) any());
        verifyNoMoreInteractions(userRequestsRepository);
    }

    /*@Configuration
    @PropertySource("classpath:com/cbinfo/testing.properties")
    public class ConstService {
        @Value("${constants.loggingMessageBeginning}")
        protected String loggingMessageBeginning;

    }*/
}
