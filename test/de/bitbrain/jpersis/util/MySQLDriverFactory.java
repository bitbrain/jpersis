package de.bitbrain.jpersis.util;

import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.mysql.MySQLDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

public class MySQLDriverFactory extends DriverFactory {
    @Override
    protected Driver createInternally(ConnectionData data) {
        return new MySQLDriver(data.host, data.port, data.db, data.username, data.password);
    }

    @Override
    protected ConnectionData extractConnectionData(GenericContainer container) {
        ConnectionData data = new ConnectionData();
        if (container instanceof MySQLContainer) {
            MySQLContainer mysql = (MySQLContainer)container;
            data.db = "test";
            data.username = mysql.getUsername();
            data.password = mysql.getPassword();
            data.host = mysql.getContainerIpAddress();
            data.port = String.valueOf(mysql.getMappedPort((Integer) mysql.getExposedPorts().get(0)));
        }
        return data;
    }

    @Override
    protected GenericContainer startDriverContainer() {
        MySQLContainer container = new MySQLContainer();
        container.start();
        return container;
    }
}
