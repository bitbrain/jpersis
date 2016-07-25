package de.bitbrain.jpersis.util;

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.sqllite.SQLiteDriver;
import org.testcontainers.containers.GenericContainer;

public class SQLiteDriverFactory extends DriverFactory {

    private final String DB;

    public SQLiteDriverFactory(String db) {
        this.DB = db;
    }
    @Override
    protected Driver createInternally(ConnectionData data) {
        return new SQLiteDriver(DB);
    }

    @Override
    protected ConnectionData extractConnectionData(GenericContainer container) {
        return null;
    }

    @Override
    protected GenericContainer startDriverContainer() {
        return null;
    }
}
