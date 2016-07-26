package de.bitbrain.jpersis;

/**
 * Can be optionally thrown by a mapper method
 */
public class MapperException extends Exception {

    public MapperException(String message) {
        super(message);
    }

    public MapperException(Throwable t) {
        super(t);
    }
}
