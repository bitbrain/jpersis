package de.bitbrain.jpersis.container;

import org.testcontainers.containers.JdbcDatabaseContainer;

public class MariaDBContainer<SELF extends MariaDBContainer<SELF>> extends JdbcDatabaseContainer<SELF> {

    public static final String NAME = "mariadb";
    public static final String IMAGE = "mariadb";
    private static final Integer MYSQL_PORT = 3306;
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    public MariaDBContainer() {
        super(IMAGE + ":latest");
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(MYSQL_PORT);
    }

    @Override
    protected void configure() {
        addExposedPort(3306);
        addEnv("MYSQL_DATABASE", "test");
        addEnv("MYSQL_USER", USER);
        addEnv("MYSQL_PASSWORD", PASSWORD);
        addEnv("MYSQL_ROOT_PASSWORD", PASSWORD);

        setCommand("mysqld");
    }

    @Override
    public String getDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mariadb://" + getContainerIpAddress() + ":" + getMappedPort(MYSQL_PORT) + "/test";
    }

    @Override
    public String getUsername() {
        return USER;
    }

    @Override
    public String getPassword() {
        return PASSWORD;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }
}