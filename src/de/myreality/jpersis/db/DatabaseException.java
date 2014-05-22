package de.myreality.jpersis.db;

/**
 * A DatabaseException is thrown when there is an error with the database. This
 * can be a violation inside of the database as well as connection errors and
 * semantic errors inside of a query. 
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
    
    
}
