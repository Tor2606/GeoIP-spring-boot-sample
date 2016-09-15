package com.cbinfo.service;

import com.cbinfo.dto.UserDataDTO;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class UserDataServiceTests {

	private String IP_FOR_TESTS = "93.75.87.81";
	private String USER_AGENT ="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.92 Safari/537.36";
	private String LOCAL_IP = "127.0.0.1";
	@Mock
	private HttpServletRequest httpServletRequestMock;

	@Mock
	private LoadingCache<String, String> countryCashMock;

	@Mock
	private UserDataService.URLWrapper ipInfoURLMock;

	@Mock
	private URLConnection urlConnectionMock;

	@Mock
	private InputStream inputStreamReaderMock;

	@Mock
	private BufferedReader bufferedReaderMock;

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

		UserDataDTO actual = userDataService.getData(httpServletRequestMock);

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
	public void readDataFromURLTest() throws IOException {
		InputStream stubInputStream = IOUtils.toInputStream("result");
		when(ipInfoURLMock.openConnection()).thenReturn(urlConnectionMock);
		when(urlConnectionMock.getInputStream()).thenReturn(stubInputStream);

		String actual = userDataService.readDataFromURL(ipInfoURLMock);

		assertThat(actual, is("result"));

		verify(ipInfoURLMock, times(1)).openConnection();
		verify(urlConnectionMock, times(1)).getInputStream();
	}

	@Test
	public void readDataFromInputStreamReaderTest() throws IOException {
		String result = "our string result";
		InputStream stubInputStream = IOUtils.toInputStream(result);
		InputStreamReader stubInputStreamReader = new InputStreamReader(stubInputStream);

		String actual = userDataService.readDataFromInputStreamReader(stubInputStreamReader);

		assertThat(actual, is(result));
	}

	@Test
	public void readDataFromInputStreamReaderWhenIOExceptionTest() throws IOException {
		String result = null;
		InputStream inputStreamMock = Mockito.mock(InputStream.class);
		when(inputStreamMock.read(any())).thenThrow(new IOException());
		InputStreamReader stubInputStreamReader = new InputStreamReader(inputStreamMock);

		String actual = userDataService.readDataFromInputStreamReader(stubInputStreamReader);

		assertThat(actual, is(result));
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
