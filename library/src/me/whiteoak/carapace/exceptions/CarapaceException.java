package me.whiteoak.carapace.exceptions;

/**
 *
 * @author White Oak
 */
public class CarapaceException extends RuntimeException {

    public CarapaceException() {
    }

    public CarapaceException(String message) {
	super(message);
    }

    public CarapaceException(String message, Throwable cause) {
	super(message, cause);
    }

    public CarapaceException(Throwable cause) {
	super(cause);
    }

}
