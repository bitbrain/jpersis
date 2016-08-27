package de.bitbrain.jpersis.util;

import de.bitbrain.jpersis.container.MariaDBContainer;
import de.bitbrain.jpersis.drivers.Driver;
import de.bitbrain.jpersis.drivers.mariadb.MariaDBDriver;
import org.testcontainers.containers.GenericContainer;

public class MariaDBDriverFactory extends DriverFactory {

    @Override
    protected Driver createInternally(ConnectionData data) {
        return new MariaDBDriver(data.host, data.port, data.db, data.username, data.password);
    }

    @Override
    protected ConnectionData extractConnectionData(GenericContainer container) {
        ConnectionData data = new ConnectionData();
        if (container instanceof MariaDBContainer) {
            MariaDBContainer mysql = (MariaDBContainer)container;
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
        MariaDBContainer container = new MariaDBContainer();
        container.start();
        return container;
    }
}
