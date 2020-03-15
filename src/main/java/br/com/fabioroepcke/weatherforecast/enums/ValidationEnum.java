package br.com.fabioroepcke.weatherforecast.enums;

/**
 * Utilizada para atribuir mensagem de erro das validações do cadastro de
 * cidade.
 *
 */
public enum ValidationEnum {

	CITY_NAME_NOT_AVAILABLE("A cidade %cidade% não está disponível para cadastro"), //
	CITY_NAME_NOT_INFORMED("O nome da cidade não foi informado"), //
	CITY_NAME_ALREADY_REGISTERED("A cidade %cidade% já foi adicionada"), //
	CITY_ID_NOT_EXIST("O identificador da cidade não existe ou já foi removido");

	private String message;

	private ValidationEnum() {

	}

	private ValidationEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
