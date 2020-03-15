package br.com.fabioroepcke.weatherforecast.exception;

/**
 * Utilizada para apontar erros de validação no cadastro de cidades.
 *
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = -7061173539835408566L;

	public ValidationException(String msg) {
		super(msg);
	}

}
