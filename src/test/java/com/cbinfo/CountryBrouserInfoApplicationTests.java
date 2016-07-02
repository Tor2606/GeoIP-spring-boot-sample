package com.cbinfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.TestCase.assertEquals;

public class CountryBrouserInfoApplicationTests {

	@Test
	public void countryServiceTest() {
		GeoIPCountryService countryService = new GeoIPCountryService();
		System.out.println(countryService.getCountry("93.75.82.147"));
		assertEquals("UA", countryService.getCountry("93.75.82.147"));
	}

}
