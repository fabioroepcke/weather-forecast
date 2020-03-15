package br.com.fabioroepcke.weatherforecast.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.fabioroepcke.weatherforecast.client.Forecast;
import br.com.fabioroepcke.weatherforecast.document.City;
import br.com.fabioroepcke.weatherforecast.enums.ValidationEnum;
import br.com.fabioroepcke.weatherforecast.exception.ValidationException;
import br.com.fabioroepcke.weatherforecast.repository.CityRepository;
import br.com.fabioroepcke.weatherforecast.utils.Utils;

@Service
public class CityService {

	private static final String COD_200 = "200";

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ForecastService forecastService;

	/**
	 * Retorna todas as cidades cadastradas em ordem ascendente.
	 * 
	 * @return Uma lista de todas as cidades cadastradas ordenada pelo nome.
	 */
	public List<City> findAll() {
		return cityRepository.findAll(Sort.by("nome").ascending());
	}

	/**
	 * Cadastra uma nova cidade com o nome informado.
	 * 
	 * @param name O nome da nova cidade.
	 * @return Um <code>Optional</code> de cidade.
	 * @throws ValidationException Caso o nome não seja informado, já exista ou não
	 *                             está disponível para cadastro no serviço de
	 *                             Forecast.
	 */
	public Optional<City> create(String name) throws ValidationException {
		checkCityNameInformed(name);
		checkCityNameNotRegistered(name);
		checkCityNameIsAvailableInApi(name);

		City city = new City();
		city.setNome(Utils.normalizeCityName(name));

		return Optional.of(cityRepository.save(city));
	}

	/**
	 * Exclui uma cidade pelo ID informado.
	 * 
	 * @param id O ID da cidade a ser excluída.
	 * @throws ValidationException Caso o ID não seja informado ou não exista.
	 */
	public void delete(String id) throws ValidationException {
		checkCityNameInformed(id);

		if (cityRepository.existsById(id)) {
			cityRepository.deleteById(id);
		} else {
			throw new ValidationException(ValidationEnum.CITY_ID_NOT_EXIST.getMessage());
		}
	}

	private void checkCityNameInformed(String name) throws ValidationException {
		if (name == null || name.isEmpty()) {
			throw new ValidationException(ValidationEnum.CITY_NAME_NOT_INFORMED.getMessage());
		}
	}

	private void checkCityNameNotRegistered(String name) throws ValidationException {
		name = Utils.normalizeCityName(name);
		if (cityRepository.findByNome(name) != null) {
			throw new ValidationException(ValidationEnum.CITY_NAME_ALREADY_REGISTERED.getMessage()
					.replaceAll("%cidade%", Utils.normalizeCityName(name)));
		}
	}

	private void checkCityNameIsAvailableInApi(String name) throws ValidationException {
		Forecast forecastFromCity = forecastService.getForecastFromCity(name);
		if (forecastFromCity == null || !forecastFromCity.getCod().equals(COD_200)) {
			throw new ValidationException(ValidationEnum.CITY_NAME_NOT_AVAILABLE.getMessage().replaceAll("%cidade%",
					Utils.normalizeCityName(name)));
		}
	}

}
