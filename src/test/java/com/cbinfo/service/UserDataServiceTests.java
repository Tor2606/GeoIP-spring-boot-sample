package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserDataServiceTests {

	private static final String IP_FOR_TESTS = "93.75.87.81";
	private static final String USER_AGENT ="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.92 Safari/537.36";
	private static final String LOCAL_IP = "127.0.0.1";
	private static final String RESULT_TEXT = "result";

	@Mock
	private HttpServletRequest httpServletRequestMock;

	@Mock
	private LoadingCache<String, String> countryCashMock;

	@InjectMocks
	private UserDataService userDataService = new UserDataService();

	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		when(httpServletRequestMock.getRemoteAddr()).thenReturn(IP_FOR_TESTS);
		when(httpServletRequestMock.getHeader("User-Agent"))
				.thenReturn(USER_AGENT);
	}

	@Test
	public void getDataMethodAndWholeServiceTest() throws Exception {
		when(countryCashMock.get(any())).thenReturn("CH");

		UserDataDTO actual = userDataService.getDataAndSave(httpServletRequestMock);

		assertThat(actual.getBrowser(), is("Chrome"));
		assertThat(actual.getCountry(), is("CH"));
		assertThat(actual.getOperatingSystem(), is("Linux"));
		assertThat(actual.getAgentFamily(), is("Chrome"));
		assertThat(actual.getDeviceCategory(), is("Personal computer"));
		assertThat(actual.getProducer(), is("Google Inc."));
		assertThat(actual.getUserAgent(), is(USER_AGENT));

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
		assertThat(actual, is("undefined"));
	}

	@Test
	public void getCountryFromJSONTest(){
		String actual = userDataService.getCountryFromJSON("{\"country\":\"UA\"}");
		assertThat(actual, is("UA"));
	}

	@Test
	public void setUserDataDTOFieldsTest() throws IOException {
		UserDataDTO userDataDTO = new UserDataDTO();

		UserDataDTO actual = userDataService.setUserDataDTOFields(userDataDTO, httpServletRequestMock);

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
