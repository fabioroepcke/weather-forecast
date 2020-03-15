package br.com.fabioroepcke.weatherforecast.utils;

import org.apache.commons.lang3.text.WordUtils;

@SuppressWarnings("deprecation")
public class Utils {

	/**
	 * Coloca todo o nome informado em <code>LowerCase</code> e deixa as primeiras
	 * letras do nome em maiúscula.
	 * 
	 * Exemplo: JaraGuá do sul -> Jaraguá Do Sul
	 * 
	 * @param name O nome informado para a normalização.
	 * @return O nome normalizado.
	 */
	public static String normalizeCityName(String name) {
		return WordUtils.capitalizeFully(name);
	}
}
