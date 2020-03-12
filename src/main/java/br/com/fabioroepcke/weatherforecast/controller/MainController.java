package br.com.fabioroepcke.weatherforecast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	/**
	 * Abre a tela de cadastro e listagem de cidades.
	 * 
	 * @param name
	 * @param model
	 * @return
	 */
	@GetMapping("/main")
	public String main(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "main";
	}
	
	
	// não esquecer de gerar um bkp do catálogo de cidades para impotar com algumas cidades já cadastradas
	
}
