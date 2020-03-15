package br.com.fabioroepcke.weatherforecast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import br.com.fabioroepcke.weatherforecast.client.ApiClient;
import br.com.fabioroepcke.weatherforecast.client.Forecast;
import br.com.fabioroepcke.weatherforecast.client.ForecastCity;
import br.com.fabioroepcke.weatherforecast.client.ForecastHour;
import br.com.fabioroepcke.weatherforecast.client.ForecastMain;
import br.com.fabioroepcke.weatherforecast.client.ForecastWeather;

@SpringBootTest
public class ForecastServiceTest {

	@Mock
	private ApiClient apiClient;

	@InjectMocks
	private ForecastService forecastService;

	@Test
	void testCityExistWhenApiReturnNull() {
		final String cityName = "Blumenau";
		doReturn(null).when(apiClient).getForecastFromCity(cityName);

		assertFalse(forecastService.isCityExists(cityName));
	}

	@Test
	void testCityExistWhenApiCodeNot200() {
		final String cityName = "Blumenau";
		final Forecast forecast = new Forecast();
		forecast.setCod("404");
		doReturn(forecast).when(apiClient).getForecastFromCity(cityName);

		assertFalse(forecastService.isCityExists(cityName));
	}

	@Test
	void testCityExistWhenApiCodeIs200() {
		final String cityName = "Blumenau";
		final Forecast forecast = new Forecast();
		forecast.setCod("200");
		doReturn(forecast).when(apiClient).getForecastFromCity(cityName);

		assertTrue(forecastService.isCityExists(cityName));
	}

	@Test
	void testGetForecastFromCityWhenApiReturnNull() {
		final String cityName = "Blumenau";
		doReturn(null).when(apiClient).getForecastFromCity(cityName);

		assertEquals(forecastService.getForecastFromCity(cityName), null);
	}

	@Test
	void testGetForecastFromCityWhenApiCodeNot200() {
		final String cityName = "Blumenau";
		final Forecast forecast = new Forecast();
		forecast.setCod("404");
		doReturn(forecast).when(apiClient).getForecastFromCity(cityName);

		assertEquals(forecastService.getForecastFromCity(cityName), null);
	}

	@Test
	void testGetForecastFromCityWhenApiCodeIs200() {
		final String cityName = "Blumenau";
		Forecast receiveJsonFomApi = createJsonFromApi();
		doReturn(receiveJsonFomApi).when(apiClient).getForecastFromCity(cityName);

		Forecast resultJsonAdjusted = createJsonResult();

		Forecast forecastFromCity = forecastService.getForecastFromCity(cityName);

		assertNotEquals(null, forecastFromCity);

		assertEquals(resultJsonAdjusted.getList().get(0).getDt_txt(), forecastFromCity.getList().get(0).getDt_txt());

		assertEquals(resultJsonAdjusted.getList().get(0).getDayOfWeek(),
				forecastFromCity.getList().get(0).getDayOfWeek());

		assertEquals(resultJsonAdjusted.getList().get(0).getDateTimeFormatted(),
				forecastFromCity.getList().get(0).getDateTimeFormatted());

		assertEquals(resultJsonAdjusted.getList().get(0).getImageUrl(),
				forecastFromCity.getList().get(0).getImageUrl());

		assertEquals(resultJsonAdjusted.getList().get(0).getWeather().get(0).getDescription(),
				forecastFromCity.getList().get(0).getWeather().get(0).getDescription());
	}

	private Forecast createJsonFromApi() {
		Forecast f = new Forecast();

		ForecastCity fc = new ForecastCity();
		fc.setName("Blumenau");
		fc.setTimezone(-10800L); // -03:00 Timezone BR

		f.setCity(fc);
		f.setCod("200");

		List<ForecastHour> fhList = new ArrayList<>();

		ForecastHour fh = new ForecastHour();
		fh.setDt_txt(LocalDateTime.parse("2020-03-15T18:00:00"));

		ForecastMain fm = new ForecastMain();
		fm.setFeels_like(BigDecimal.valueOf(34.44));
		fm.setHumidity(85);
		fm.setTemp(BigDecimal.valueOf(29.94));
		fm.setTemp_max(BigDecimal.valueOf(29.94));
		fm.setTemp_min(BigDecimal.valueOf(25.75));

		fh.setMain(fm);

		List<ForecastWeather> fwList = new ArrayList<>();

		ForecastWeather fw = new ForecastWeather();
		fw.setDescription("céu limpo");
		fw.setIcon("01d");

		fwList.add(fw);

		fh.setWeather(fwList);

		fhList.add(fh);

		f.setList(fhList);

		return f;
	}

	private Forecast createJsonResult() {
		Forecast createJsonFromApi = createJsonFromApi();
		createJsonFromApi.getList().get(0).setDt_txt(LocalDateTime.parse("2020-03-15T15:00:00"));
		createJsonFromApi.getList().get(0).setDayOfWeek("Domingo");
		createJsonFromApi.getList().get(0).setDateTimeFormatted("15/03/2020 às 15:00");
		createJsonFromApi.getList().get(0).setImageUrl("http://openweathermap.org/img/w/01d.png");
		createJsonFromApi.getList().get(0).getWeather().get(0).setDescription(
				StringUtils.capitalize(createJsonFromApi.getList().get(0).getWeather().get(0).getDescription()));
		return createJsonFromApi;
	}

}
