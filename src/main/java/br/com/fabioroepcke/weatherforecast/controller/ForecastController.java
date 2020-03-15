package br.com.fabioroepcke.weatherforecast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fabioroepcke.weatherforecast.client.Forecast;
import br.com.fabioroepcke.weatherforecast.service.ForecastService;

@Controller
public class ForecastController {

	private static final String COD_200 = "200";

	@Autowired
	private ForecastService clientApiService;

	/**
	 * Abre a tela de previsao da cidade informada.
	 */
	@RequestMapping(value = "/forecast", method = RequestMethod.GET)
	public String forecast(@RequestParam(name = "cityName") String cityName, Model model) {

		Forecast forecastFromCity = clientApiService.getForecastFromCity(cityName);

		if (forecastFromCity == null || !forecastFromCity.getCod().equals(COD_200)) {
			return "redirect:/";
		}

		model.addAttribute("forecast", forecastFromCity);

		return "forecast";
	}

}
