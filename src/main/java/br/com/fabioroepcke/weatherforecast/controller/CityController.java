package br.com.fabioroepcke.weatherforecast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fabioroepcke.weatherforecast.document.City;
import br.com.fabioroepcke.weatherforecast.exception.ValidationException;
import br.com.fabioroepcke.weatherforecast.service.CityService;

@Controller
public class CityController {

	@Autowired
	private CityService cityService;

	/**
	 * Abre a tela de cadastro e listagem de cidades.
	 */
	@GetMapping("/")
	public String cities(Model model) {

		model.addAttribute("cities", cityService.findAll());
		model.addAttribute("city", new City());

		return "cities";
	}

	/**
	 * Adicionar uma cidade e recarregar a tela com a lista atualizada.
	 */
	@PostMapping("/")
	public String addCity(@ModelAttribute City city, Model model) {

		try {
			cityService.create(city.getNome());
		} catch (ValidationException e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("cities", cityService.findAll());
		model.addAttribute("city", new City());

		return "cities";
	}

	/**
	 * Remover uma cidade e recarregar a tela com a lista atualizada.
	 */
	@RequestMapping(value = "/delete_city", method = RequestMethod.GET)
	public String delCity(@RequestParam(name = "cityId") String cityId, Model model) {
		try {
			cityService.delete(cityId);
		} catch (ValidationException e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("cities", cityService.findAll());
		model.addAttribute("city", new City());

		return "cities";
	}

}
