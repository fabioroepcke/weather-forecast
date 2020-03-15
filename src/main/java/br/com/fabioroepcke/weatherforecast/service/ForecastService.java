package br.com.fabioroepcke.weatherforecast.service;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.fabioroepcke.weatherforecast.client.ApiClient;
import br.com.fabioroepcke.weatherforecast.client.Forecast;
import br.com.fabioroepcke.weatherforecast.client.ForecastHour;

@Service
public class ForecastService {

	private static final String COD_200 = "200";
	private static final String IMG_URL = "http://openweathermap.org/img/w/%icon%.png";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

	@Autowired
	private ApiClient apiClient;

	/**
	 * Verifica se a cidade informada existe na Api.
	 * 
	 * @param cityName A cidade a ser verificada a existência na Api.
	 * @return <code>true</code> se a cidade existir, <code>false</code> caso
	 *         contrário.
	 */
	public boolean isCityExists(String cityName) {
		Forecast forecastFromCity = apiClient.getForecastFromCity(cityName);
		if (forecastFromCity != null && forecastFromCity.getCod().equals(COD_200)) {
			return true;
		}
		return false;
	}

	/**
	 * Obtém os dados da previsão do tempo para os próximos 5 dias com previsão de 3
	 * em 3 horas. Sempre retorna 40 registros.
	 * 
	 * @param cityName A cidade que se quer obter a previsão do tempo.
	 * @return A previsão do tempo da cidade informada ou <code>null</code> se não
	 *         obter a previsão.
	 */
	public Forecast getForecastFromCity(String cityName) {
		Forecast forecastFromCity = apiClient.getForecastFromCity(cityName);
		if (forecastFromCity != null && forecastFromCity.getCod().equals(COD_200)) {

			adjustAndFormatting(forecastFromCity);

			return forecastFromCity;
		}
		return null;
	}

	private void adjustAndFormatting(Forecast forecastFromCity) {
		for (ForecastHour forecastHour : forecastFromCity.getList()) {
			adjustTimezone(forecastHour, forecastFromCity.getCity().getTimezone());
			defineDayOfWeek(forecastHour);
			dateTimeFormatter(forecastHour);

			if (!forecastHour.getWeather().isEmpty()) {
				defineIconUrl(forecastHour);
				adjustDescription(forecastHour);
			} else {
				forecastHour.setImageUrl("");
			}
		}
	}

	private void adjustTimezone(ForecastHour forecastHour, Long timezone) {
		forecastHour.setDt_txt(forecastHour.getDt_txt().plusSeconds(timezone));
	}

	private void adjustDescription(ForecastHour forecastHour) {
		forecastHour.getWeather().get(0).setDescription(
				StringUtils.capitalize(forecastHour.getWeather().get(0).getDescription().toLowerCase()));
	}

	private void defineIconUrl(ForecastHour forecastHour) {
		forecastHour.setImageUrl(IMG_URL.replaceAll("%icon%", forecastHour.getWeather().get(0).getIcon()));
	}

	private void dateTimeFormatter(ForecastHour forecastHour) {
		forecastHour.setDateTimeFormatted(forecastHour.getDt_txt().format(DATE_TIME_FORMATTER));
	}

	private void defineDayOfWeek(ForecastHour forecastHour) {
		DayOfWeek d = forecastHour.getDt_txt().getDayOfWeek();
		switch (d) {
		case SUNDAY:
			forecastHour.setDayOfWeek("Domingo");
			break;
		case MONDAY:
			forecastHour.setDayOfWeek("Segunda");
			break;
		case TUESDAY:
			forecastHour.setDayOfWeek("Terça");
			break;
		case WEDNESDAY:
			forecastHour.setDayOfWeek("Quarta");
			break;
		case THURSDAY:
			forecastHour.setDayOfWeek("Quinta");
			break;
		case FRIDAY:
			forecastHour.setDayOfWeek("Sexta");
			break;
		case SATURDAY:
		default:
			forecastHour.setDayOfWeek("Sábado");
			break;
		}
	}

}
