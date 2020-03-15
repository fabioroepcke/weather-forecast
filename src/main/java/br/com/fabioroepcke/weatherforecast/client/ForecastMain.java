package br.com.fabioroepcke.weatherforecast.client;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastMain {

	private BigDecimal temp;

	private BigDecimal feels_like;

	private BigDecimal temp_min;

	private BigDecimal temp_max;

	private Integer humidity;

}
