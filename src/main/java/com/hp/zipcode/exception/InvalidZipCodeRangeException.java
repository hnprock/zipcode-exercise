package com.hp.zipcode.exception;

/**
 * The InvalidZipcodeRangeException is thrown when the ZipCodeService detects an invalid zip code range.
 * 
 * @author Huy Pham
 *
 */
public class InvalidZipCodeRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1023072187931375009L;

	/**
     * Constructs a new exception with the specified detail message.
     *
     * @param   message   the detail message.
     */
    public InvalidZipCodeRangeException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * @param  message the detail message.
     * @param  cause the cause
     */
    public InvalidZipCodeRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
