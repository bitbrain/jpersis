package de.myreality.jpersis;

/**
 * Is thrown when a specific mapper can not be found or an annotation is not
 * well used.
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class MapperException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * Constructor for the exception
     * 
     * @param message Message to set
     */
    public MapperException(String message) {
        super(message);
    }
}
