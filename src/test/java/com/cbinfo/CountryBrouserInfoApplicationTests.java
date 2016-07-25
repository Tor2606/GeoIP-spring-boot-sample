package com.cbinfo;

import com.cbinfo.service.GeoIPCountryService;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
public class CountryBrouserInfoApplicationTests {

	@Test
	public void countryServiceTest() throws IOException {
		GeoIPCountryService countryService = new GeoIPCountryService();
		System.out.println(countryService.getData("93.75.82.147","Opera").getCountryCode());
		assertEquals("UA", countryService.getData("93.75.82.147","Opera").getCountryCode());

	}

}
