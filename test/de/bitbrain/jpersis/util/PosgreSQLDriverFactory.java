package de.bitbrain.jpersis.util;

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.postgresql.PostgreSQLDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class PosgreSQLDriverFactory extends DriverFactory {
    @Override
    protected Driver createInternally(ConnectionData data) {
        return new PostgreSQLDriver(data.host, data.port, data.db, data.username, data.password);
    }

    @Override
    protected ConnectionData extractConnectionData(GenericContainer container) {
        ConnectionData data = new ConnectionData();
        if (container instanceof PostgreSQLContainer) {
            PostgreSQLContainer postgres = (PostgreSQLContainer)container;
            data.db = "postgres";
            data.username = postgres.getUsername();
            data.password = postgres.getPassword();
            data.host = postgres.getContainerIpAddress();
            data.port = String.valueOf(postgres.getMappedPort((Integer)postgres.getExposedPorts().get(0)));
        }
        return data;
    }

    @Override
    protected GenericContainer startDriverContainer() {
        PostgreSQLContainer container = new PostgreSQLContainer();
        container.start();
        return container;
    }
}
