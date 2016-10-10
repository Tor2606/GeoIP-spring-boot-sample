package com.cbinfo.service;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequest;
import com.cbinfo.repository.UserRequestsRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceTests {

    private static final String LOGGING_MESSAGE_BEGINNING = "Incoming request(time, ip, url, users email): ";
    private static final String URI_VALUE = "uri";
    private static final String IP_VALUE = "ip";
    private static final String EMAIL_VALUE = "ip";
    private static final String DELIMITER = ", ";


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
        UserRequest userRequest = new UserRequest();
        doNothing().when(spyRequestService).setRequestModelFields(any(), (HttpServletRequest) any());
        doNothing().when(spyRequestService).saveRequestModel(any());

        spyRequestService.saveHttpServletRequest((HttpServletRequest) any());

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
        RequestService spyRequestService = Mockito.spy(RequestService.class);
        doReturn(EMAIL_VALUE).when(spyRequestService).getUserEmail();
        String expectedDate = new Date().toString();//

        String actual = spyRequestService.getLoggingMessage(httpServletRequestMock);

        assertThat(actual, is(expectedMessage(expectedDate)));
        verify(spyRequestService, times(1)).getUserEmail();
        verify(spyRequestService, times(1)).getLoggingMessage(any());
        verifyNoMoreInteractions(spyRequestService);
    }

    private String expectedMessage(String date) {
        return LOGGING_MESSAGE_BEGINNING + date + DELIMITER
                + IP_VALUE + DELIMITER + URI_VALUE + DELIMITER + EMAIL_VALUE;
    }

    @Test
    public void saveRequestModelTest() {
        UserRequest userRequest = new UserRequest();

        requestService.saveRequestModel(userRequest);

        verify(userRequestsRepository, times(1)).save((UserRequest) any());
        verifyNoMoreInteractions(userRequestsRepository);
    }
}
