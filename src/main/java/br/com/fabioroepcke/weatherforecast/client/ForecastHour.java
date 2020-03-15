package br.com.fabioroepcke.weatherforecast.client;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastHour {

	private ForecastMain main;

	private List<ForecastWeather> weather;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dt_txt;

	private String dateTimeFormatted;

	private String dayOfWeek;

	private String imageUrl;

}
