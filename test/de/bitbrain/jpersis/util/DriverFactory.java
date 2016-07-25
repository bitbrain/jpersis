package de.bitbrain.jpersis.util;

import de.bitbrain.jpersis.drivers.Driver;
import org.testcontainers.containers.GenericContainer;

public abstract class DriverFactory {

    private GenericContainer container;

    private Driver driver;

    public Driver create() {
        if (driver == null) {
            if (container == null) {
                container = startDriverContainer();
            }
            ConnectionData data = extractConnectionData(container);
            driver = createInternally(data);
        }
        return driver;
    }

    protected abstract Driver createInternally(ConnectionData data);
    protected abstract ConnectionData extractConnectionData(GenericContainer container);
    protected abstract GenericContainer startDriverContainer();
}
