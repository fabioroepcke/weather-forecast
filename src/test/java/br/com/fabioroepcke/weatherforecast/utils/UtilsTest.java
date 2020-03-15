package br.com.fabioroepcke.weatherforecast.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilsTest {

	@Test
	void testFindAllNotExpectedException() {
		assertEquals("Jaraguá Do Sul", Utils.normalizeCityName("jarAguÁ do suL"));
	}

}
