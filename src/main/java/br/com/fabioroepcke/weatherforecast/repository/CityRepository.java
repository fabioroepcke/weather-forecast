package br.com.fabioroepcke.weatherforecast.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.fabioroepcke.weatherforecast.document.City;

@Repository
public interface CityRepository extends MongoRepository<City, String> {

	public String findByNome(String nome);

}
