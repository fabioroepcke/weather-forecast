package br.com.fabioroepcke.weatherforecast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import br.com.fabioroepcke.weatherforecast.client.Forecast;
import br.com.fabioroepcke.weatherforecast.document.City;
import br.com.fabioroepcke.weatherforecast.enums.ValidationEnum;
import br.com.fabioroepcke.weatherforecast.exception.ValidationException;
import br.com.fabioroepcke.weatherforecast.repository.CityRepository;
import br.com.fabioroepcke.weatherforecast.utils.Utils;

@SpringBootTest
public class CityServiceTest {

	@Mock
	private CityRepository cityRepository;

	@Mock
	private ForecastService forecastService;

	@InjectMocks
	private CityService cityService;

	@Test
	void testFindAllNotExpectedException() {
		List<City> cityList = new ArrayList<>();
		doReturn(cityList).when(cityRepository).findAll(Sort.by("nome").ascending());

		Assertions.assertDoesNotThrow(() -> {
			List<City> findAll = cityService.findAll();

			assertTrue(findAll.isEmpty());
		});
	}

	@Test
	void testExpectedExceptionOnDeleteCityInformedNull() {
		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.delete(null);
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_INFORMED.getMessage());
	}

	@Test
	void testExpectedExceptionOnDeleteCityInformedEmpty() {
		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.delete("");
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_INFORMED.getMessage());
	}

	@Test
	void testExpectedExceptionOnDeleteCityInformedCorrectButNotExist() {
		doReturn(false).when(cityRepository).existsById("abcd");

		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.delete("abcd");
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_ID_NOT_EXIST.getMessage());
	}

	@Test
	void testNotExpectedExceptionOnDeleteCityInformedCorrectAndExist() {
		doReturn(true).when(cityRepository).existsById("abcd");
		doNothing().when(cityRepository).deleteById("abcd");

		Assertions.assertDoesNotThrow(() -> {
			cityService.delete("abcd");
		});

	}

	@Test
	void testExpectedExceptionOnCreateCityInformedNull() {
		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.create(null);
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_INFORMED.getMessage());
	}

	@Test
	void testExpectedExceptionOnCreateCityInformedEmpty() {
		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.create("");
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_INFORMED.getMessage());
	}

	@Test
	void testExpectedExceptionOnCreateCityInformedCorrectButCityAlreadyRegistered() {
		final String cityName = "Blumenau";
		doReturn(cityName).when(cityRepository).findByNome(cityName);

		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.create(cityName);
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_ALREADY_REGISTERED.getMessage()
				.replaceAll("%cidade%", Utils.normalizeCityName(cityName)));
	}

	@Test
	void testExpectedExceptionOnCreateCityInformedCorrectAndCityNotRegisteredAndApiReturnNull() {
		final String cityName = "Blumenau";
		doReturn(null).when(cityRepository).findByNome(cityName);

		doReturn(null).when(forecastService).getForecastFromCity(cityName);

		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.create(cityName);
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_AVAILABLE.getMessage()
				.replaceAll("%cidade%", Utils.normalizeCityName(cityName)));
	}

	@Test
	void testExpectedExceptionOnCreateCityInformedCorrectAndCityNotRegisteredAndApiCodeNot200() {
		final String cityName = "Blumenau";
		doReturn(null).when(cityRepository).findByNome(cityName);

		final Forecast forecast = new Forecast();
		forecast.setCod("404");
		doReturn(forecast).when(forecastService).getForecastFromCity(cityName);

		ValidationException assertThrows = Assertions.assertThrows(ValidationException.class, () -> {
			cityService.create(cityName);
		});
		assertEquals(assertThrows.getMessage(), ValidationEnum.CITY_NAME_NOT_AVAILABLE.getMessage()
				.replaceAll("%cidade%", Utils.normalizeCityName(cityName)));
	}

	@Test
	void testNotExpectedExceptionOnCreateCityInformedCorrectAndCityNotRegisteredAndApiCodeIs200() {
		final String cityName = "Blumenau";
		doReturn(null).when(cityRepository).findByNome(cityName);

		City city = new City();
		city.setNome(Utils.normalizeCityName(cityName));
		when(cityRepository.save(any(City.class))).thenReturn(city);

		final Forecast forecast = new Forecast();
		forecast.setCod("200");
		doReturn(forecast).when(forecastService).getForecastFromCity(cityName);

		Assertions.assertDoesNotThrow(() -> {
			Optional<City> cityCreateOpt = cityService.create(cityName);

			assertEquals(cityCreateOpt.get().getNome(), city.getNome());
		});
	}

}
