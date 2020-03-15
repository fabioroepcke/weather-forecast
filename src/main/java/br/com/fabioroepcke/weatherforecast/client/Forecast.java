package br.com.fabioroepcke.weatherforecast.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

	private String cod;

	private List<ForecastHour> list;

	private ForecastCity city;

}
