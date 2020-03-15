package br.com.fabioroepcke.weatherforecast.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

	private String API_KEY = "eb8b1a9405e659b2ffc78f0a520b1a46";
	private String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast?q=%cityName%&mode=json&units=metric&lang=pt&appid=%apiKey%";

	/**
	 * Busca na API os dados do clima para a cidade informada.
	 * 
	 * @param cityName A cidade que deve ser retornados os dados do clima.
	 * @return Os cados do clima da cidade informada ou Nulo se não for possível
	 *         obter os dados da API.
	 */
	public Forecast getForecastFromCity(String cityName) {
		Forecast forecast;
		try {
			String url = URL_BASE;
			url = url.replaceAll("%cityName%", cityName);
			url = url.replaceAll("%apiKey%", API_KEY);

			RestTemplate restTemplate = new RestTemplate();
			forecast = restTemplate.getForObject(url, Forecast.class);
		} catch (Exception e) {
			return null;
		}
		return forecast;
	}
}
